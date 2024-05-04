package com.huaiyin.pytorch.service.impl;

import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.ImageDTO;
import com.huaiyin.pytorch.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * ClassName:CommonServiceImpl
 * Package:com.huaiyin.pytorch.service.impl
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/5/1 16:53
 * @Version 1.0
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
	@Value("${image.upload}")
	private String basePath;
	@Value("${image.recognize}")
	private String recognizePath;

	@Override
	public ApiResponse<ImageDTO> upload(MultipartFile file) {
		// file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
		// file的原始文件名file.getOriginalFilename()
		String original = file.getOriginalFilename();

		// 获取文件后缀名
		String suffix = original.substring(original.lastIndexOf("."));
		// 判断文件后缀名是否正确,是不是jpg,png,jpeg格式
		if (!suffix.equalsIgnoreCase(".jpg") && !suffix.equalsIgnoreCase(".png") && !suffix.equalsIgnoreCase(".jpeg")) {
			return ApiResponse.error("文件格式不正确");
		}
		// 使用UUID生成文件名,防止文件名重复造成文件覆盖
		String fileName = UUID.randomUUID().toString() + suffix;
        fileName = fileName.replaceAll("-", "");
		// 生成一个返回给前端的文件名,不带后缀名
		String uploadFile = fileName.substring(0, fileName.lastIndexOf("."));
		// 创建一个目录对象
		File dir = new File(basePath);
		// 判断当前目录是否存在
		if (!dir.exists()) {
			// 目录不存在需要创建
			dir.mkdirs();
		}
		log.info(file.toString());
		try {
			// 将临时文件存储到本地
			file.transferTo(new File(basePath + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return ApiResponse.error("文件上传失败");
		}finally{
			// 释放资源
			file = null;
		}
		// 封装返回给前端的图片对象
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFile(uploadFile);
		return ApiResponse.success(imageDTO);
	}

	/**
	 * 读取原始图片
	 *
	 * @param name
	 * @param response
	 */
	@Override
	public void download(String name, HttpServletResponse response) {
		// 根据文件名判断到底是哪个文件
		readFile(name, response, basePath);
	}

	/**
	 * 识别后的图片路径
	 *
	 * @param name
	 * @param response
	 */
	@Override
	public void recognize(String name, HttpServletResponse response) {
		// 根据文件名判断到底是哪个文件
		readFile(name, response, recognizePath);
	}

	private void readFile(String name, HttpServletResponse response, String filePath) {
		Path dir = Paths.get(filePath);
		Stream<Path> stream = null;
		FileInputStream fileInputStream = null;
		ServletOutputStream outputStream = null;
		try {
			stream = Files.list(dir);
			Optional<Path> opt = stream
					.filter(path -> !Files.isDirectory(path))
					.filter(path -> {
						String fileWithoutExtension = com.google.common.io.Files.getNameWithoutExtension(path.toString());
						return fileWithoutExtension.equals(name);
					})
					.findFirst();
			if (!opt.isPresent()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			String fileName = opt.get().getFileName().toString();
			// 输入流读取文件内容
			fileInputStream = new FileInputStream(new File(filePath + fileName));
			outputStream = response.getOutputStream();
			// 设置响应头
			response.setContentType("image/jpeg");
			byte[] bytes = new byte[1024 * 10];// 一次性最大读取10kb
			int len = 0;
			while ((len = fileInputStream.read(bytes)) != -1) {
				// 输出流将文件内容写入到浏览器
				outputStream.write(bytes, 0, len);
				outputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
			// 流的关闭,出现异常的时候进行关闭流的操作
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		} finally {
			if (stream != null)
				stream.close();
		}
	}
}
