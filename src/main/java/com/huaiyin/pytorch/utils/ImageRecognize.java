package com.huaiyin.pytorch.utils;


import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import com.huaiyin.pytorch.config.ODConfig;
import com.huaiyin.pytorch.dto.model.Detection;
import com.huaiyin.pytorch.entity.Record;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ClassName:ImageRecognize
 * Package:com.huaiyin.pytorch.utils
 * Description:
 * 图像识别工具类
 * @Author 卜翔威
 * @Create 2024/5/1 14:04
 * @Version 1.0
 */
public class ImageRecognize {
	static {
		// 加载opencv动态库，
//		System.load(ClassLoader.getSystemResource("lib/opencv_videoio_ffmpeg470_64.dll").getPath());
		nu.pattern.OpenCV.loadLocally();
	}
	/**
	 * 图像识别
	 * @param modelPath 模型路径
	 * @param confThreshold 置信度
	 * @param nmsThreshold  nms阈值
	 * @param imagePath 图片路径
	 * @return 识别后的图片地址
	 */
	public static Record imageRecognize(String modelPath, float confThreshold, float nmsThreshold, String imagePath,String recognizePath) throws OrtException{
		List<double[]> colors = new ArrayList<>();
		String[] labels = null;
		// 加载ONNX模型
		OrtEnvironment environment = OrtEnvironment.getEnvironment();
		OrtSession.SessionOptions sessionOptions = new OrtSession.SessionOptions();

		// 使用gpu,需要本机按钻过cuda，并修改pom.xml，不安装也能运行本程序
//		 sessionOptions.addCUDA(0);

		OrtSession session = environment.createSession(modelPath, sessionOptions);
		String meteStr = session.getMetadata().getCustomMetadata().get("names");


		labels = new String[meteStr.split(",").length];


		Pattern pattern = Pattern.compile("'([^']*)'");
		Matcher matcher = pattern.matcher(meteStr);

		int h = 0;
		while (matcher.find()) {
			labels[h] = matcher.group(1);
			Random random = new Random();
			double[] color = {random.nextDouble() * 256, random.nextDouble() * 256, random.nextDouble() * 256};
			colors.add(color);
			h++;
		}
		// 输出基本信息
		session.getInputInfo().keySet().forEach(x -> {
			try {
				System.out.println("input name = " + x);
				System.out.println(session.getInputInfo().get(x).getInfo().toString());
			} catch (OrtException e) {
				throw new RuntimeException(e);
			}
		});


		Map<String, String> map = getImagePathMap(imagePath);
		Record record = new Record();
		for (String fileName : map.keySet()) {
			String imageFilePath = map.get(fileName);
			System.out.println(imageFilePath);
			// 读取 image
			Mat img = Imgcodecs.imread(imageFilePath);
			Mat image = img.clone();
			Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);


			// 在这里先定义下框的粗细、字的大小、字的类型、字的颜色(按比例设置大小粗细比较好一些)
			int minDwDh = Math.min(img.width(), img.height());
			int thickness = minDwDh / ODConfig.lineThicknessRatio;
			long start_time = System.currentTimeMillis();
			// 更改 image 尺寸
			Letterbox letterbox = new Letterbox();
			image = letterbox.letterbox(image);

			double ratio = letterbox.getRatio();
			double dw = letterbox.getDw();
			double dh = letterbox.getDh();
			int rows = letterbox.getHeight();
			int cols = letterbox.getWidth();
			int channels = image.channels();

			// 将Mat对象的像素值赋值给Float[]对象
			float[] pixels = new float[channels * rows * cols];
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					double[] pixel = image.get(j, i);
					for (int k = 0; k < channels; k++) {
						// 这样设置相当于同时做了image.transpose((2, 0, 1))操作
						pixels[rows * cols * k + j * cols + i] = (float) pixel[k] / 255.0f;
					}
				}
			}

			// 创建OnnxTensor对象
			long[] shape = {1L, (long) channels, (long) rows, (long) cols};
			OnnxTensor tensor = OnnxTensor.createTensor(environment, FloatBuffer.wrap(pixels), shape);
			HashMap<String, OnnxTensor> stringOnnxTensorHashMap = new HashMap<>();
			stringOnnxTensorHashMap.put(session.getInputInfo().keySet().iterator().next(), tensor);

			// 运行推理
			OrtSession.Result output = session.run(stringOnnxTensorHashMap);
			float[][] outputData = ((float[][][]) output.get(0).getValue())[0];

			outputData = transposeMatrix(outputData);
			Map<Integer, List<float[]>> class2Bbox = new HashMap<>();

			for (float[] bbox : outputData) {


				float[] conditionalProbabilities = Arrays.copyOfRange(bbox, 4, outputData.length);
				int label = argmax(conditionalProbabilities);
				float conf = conditionalProbabilities[label];
				if (conf < confThreshold) continue;

				bbox[4] = conf;

				// xywh to (x1, y1, x2, y2)
				xywh2xyxy(bbox);

				// skip invalid predictions
				if (bbox[0] >= bbox[2] || bbox[1] >= bbox[3]) continue;


				class2Bbox.putIfAbsent(label, new ArrayList<>());
				class2Bbox.get(label).add(bbox);
			}

			List<Detection> detections = new ArrayList<>();
			for (Map.Entry<Integer, List<float[]>> entry : class2Bbox.entrySet()) {
				int label = entry.getKey();
				List<float[]> bboxes = entry.getValue();
				bboxes = nonMaxSuppression(bboxes, nmsThreshold);
				for (float[] bbox : bboxes) {
					String labelString = labels[label];
					detections.add(new Detection(labelString, entry.getKey(), Arrays.copyOfRange(bbox, 0, 4), bbox[4]));
				}
			}

			// 保存已经添加的标签的位置
			List<Rect> addedLabels = new ArrayList<>();
			for (Detection detection : detections) {
				float[] bbox = detection.getBbox();
				System.out.println(detection.toString());
				// 画框
				Point topLeft = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio);
				Point bottomRight = new Point((bbox[2] - dw) / ratio, (bbox[3] - dh) / ratio);
				Scalar color = new Scalar(colors.get(detection.getClsId()));
				Imgproc.rectangle(img, topLeft, bottomRight, color, thickness);
				// 框上写文字
				Point boxNameLoc = new Point((bbox[0] - dw) / ratio, (bbox[1] - dh) / ratio - 3);
				// 获取预测率
				float confidence = detection.confidence;
				// 将预测率转换为字符串，并保留两位小数
				String confidenceStr = String.format("%.2f", confidence);
				// 将预测率添加到标签字符串中
				String labelWithConfidence = detection.getLabel() + ": " + confidenceStr;

				// 计算文本的宽度和高度
				int[] baseLine = new int[1];
				// 动态调整字体大小
				double fontSize = 0.7;
				Size labelSize = Imgproc.getTextSize(labelWithConfidence,Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, thickness, baseLine);
//				// 创建一个新的矩形来表示标签的位置
//				Rect labelRect = new Rect(boxNameLoc, new Size(labelSize.width, labelSize.height + baseLine[0]));
//				// 检查新的标签位置是否与已经添加的标签重叠
//				for (Rect addedLabel : addedLabels) {
//					if (intersectsWith(labelRect,addedLabel)) {
//						// 如果重叠，找到一个新的位置
//						boxNameLoc.y += labelSize.height + baseLine[0] + thickness;
//						labelRect = new Rect(boxNameLoc, new Size(labelSize.width, labelSize.height + baseLine[0]));
//					}
//				}
//				// 将新的标签位置添加到列表中
//				addedLabels.add(labelRect);
				// 在文本的位置画一个填充的矩形作为文本的背景
				Imgproc.rectangle(img, boxNameLoc, new Point(boxNameLoc.x + labelSize.width, boxNameLoc.y - labelSize.height - baseLine[0]), color, Core.FILLED);
				// 在图像上添加白色的文本
				Imgproc.putText(img, labelWithConfidence, boxNameLoc, Imgproc.FONT_HERSHEY_SIMPLEX, fontSize, new Scalar(255, 255, 255), thickness);
			}
            Long recognizeTime = System.currentTimeMillis() - start_time;
			// System.out.printf("time：%d ms.", (System.currentTimeMillis() - start_time));
			// 保存图像到同级目录
			String recognizeFile = UUID.randomUUID().toString().replaceAll("-","") +
					imagePath.substring(imagePath.lastIndexOf("."));
			String path = recognizePath + recognizeFile;
			Imgcodecs.imwrite(path, img);
			record.setRecognizeTime(recognizeTime);
			// todo 保存识别结果,不含图片后缀名
//			record.setNewAddress(recognizeFile.substring(0,recognizeFile.lastIndexOf(".")));
			record.setNewAddress(recognizeFile);
		}
		return record;
	}

	public static void scaleCoords(float[] bbox, float orgW, float orgH, float padW, float padH, float gain) {
		// xmin, ymin, xmax, ymax -> (xmin_org, ymin_org, xmax_org, ymax_org)
		bbox[0] = Math.max(0, Math.min(orgW - 1, (bbox[0] - padW) / gain));
		bbox[1] = Math.max(0, Math.min(orgH - 1, (bbox[1] - padH) / gain));
		bbox[2] = Math.max(0, Math.min(orgW - 1, (bbox[2] - padW) / gain));
		bbox[3] = Math.max(0, Math.min(orgH - 1, (bbox[3] - padH) / gain));
	}

	public static void xywh2xyxy(float[] bbox) {
		float x = bbox[0];
		float y = bbox[1];
		float w = bbox[2];
		float h = bbox[3];

		bbox[0] = x - w * 0.5f;
		bbox[1] = y - h * 0.5f;
		bbox[2] = x + w * 0.5f;
		bbox[3] = y + h * 0.5f;
	}

	public static float[][] transposeMatrix(float[][] m) {
		float[][] temp = new float[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

	public static List<float[]> nonMaxSuppression(List<float[]> bboxes, float iouThreshold) {

		List<float[]> bestBboxes = new ArrayList<>();

		bboxes.sort(Comparator.comparing(a -> a[4]));

		while (!bboxes.isEmpty()) {
			float[] bestBbox = bboxes.remove(bboxes.size() - 1);
			bestBboxes.add(bestBbox);
			bboxes = bboxes.stream().filter(a -> computeIOU(a, bestBbox) < iouThreshold).collect(Collectors.toList());
		}

		return bestBboxes;
	}

	public static float computeIOU(float[] box1, float[] box2) {

		float area1 = (box1[2] - box1[0]) * (box1[3] - box1[1]);
		float area2 = (box2[2] - box2[0]) * (box2[3] - box2[1]);

		float left = Math.max(box1[0], box2[0]);
		float top = Math.max(box1[1], box2[1]);
		float right = Math.min(box1[2], box2[2]);
		float bottom = Math.min(box1[3], box2[3]);

		float interArea = Math.max(right - left, 0) * Math.max(bottom - top, 0);
		float unionArea = area1 + area2 - interArea;
		return Math.max(interArea / unionArea, 1e-8f);

	}

	//返回最大值的索引
	public static int argmax(float[] a) {
		float re = -Float.MAX_VALUE;
		int arg = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] >= re) {
				re = a[i];
				arg = i;
			}
		}
		return arg;
	}

	public static Map<String, String> getImagePathMap(String imagePath) {
		Map<String, String> map = new TreeMap<>();
		File file = new File(imagePath);
		if (file.isFile()) {
			map.put(file.getName(), file.getAbsolutePath());
		} else if (file.isDirectory()) {
			for (File tmpFile : Objects.requireNonNull(file.listFiles())) {
				map.putAll(getImagePathMap(tmpFile.getPath()));
			}
		}
		return map;
	}
}

