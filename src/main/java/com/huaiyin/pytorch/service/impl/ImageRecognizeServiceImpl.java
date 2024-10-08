package com.huaiyin.pytorch.service.impl;

import ai.onnxruntime.OrtException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.ModelForm;
import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.entity.Record;
import com.huaiyin.pytorch.entity.User;
import com.huaiyin.pytorch.mapper.ImageMapper;
import com.huaiyin.pytorch.service.ImageRecognizeService;
import com.huaiyin.pytorch.utils.ImageRecognize;
import com.huaiyin.pytorch.utils.UserHolder;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
 * ClassName:ImageRecognizeServiceImpl
 * Package:com.huaiyin.pytorch.service.impl
 * Description:
 * 图像识别类实现类
 *
 * @Author 卜翔威
 * @Create 2024/5/1 13:39
 * @Version 1.0
 */

@Service
@Slf4j
public class ImageRecognizeServiceImpl extends ServiceImpl<ImageMapper, Record> implements ImageRecognizeService {

	@Value("${image.upload}")
	private String basePath;
	@Value("${image.recognize}")
	private String recognizePath;
	@Value("${model.path}")
	private String modelPath;

	@Override
	public ApiResponse<Record> recognize(ModelForm file, HttpServletRequest request) {
		// todo 完整的获取文件名
		try {

		    String fileName = file.getImage();
			// 图像识别
			Record imageRecognize = ImageRecognize.imageRecognize(modelPath,
					file.getConfThreshold(), file.getNmsThreshold(), basePath + fileName, recognizePath);
			Long userId = (Long) request.getSession().getAttribute("user");
			imageRecognize.setOriginalAddress(file.getImage());
			imageRecognize.setUserId(userId);
			save(imageRecognize);
			return ApiResponse.success(imageRecognize);
		}catch(OrtException e) {
			log.error("模型加载失败", e);
			return ApiResponse.error("模型加载失败");
		}
	}
	/**
	 * 分页查询
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@Override
	public ApiResponse<Page> list(int page, int pageSize,HttpServletRequest request) {
		// 构造分页构造器
		Page pageInfo = new Page(page, pageSize);
		// 条件构造器
		LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
		Long userId = (Long)request.getSession().getAttribute("user");
		queryWrapper.eq(Record::getUserId,userId);
		// 添加排序条件
		queryWrapper.orderByDesc(Record::getCreateTime);
		// 执行查询
		page(pageInfo, queryWrapper);
		if (pageInfo.getRecords().isEmpty()) {
			return ApiResponse.error("没有数据");
		}
		return ApiResponse.success(pageInfo);
	}

	/**
	 * 根据id查询记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ApiResponse<Record> getRecordById(Long id) {
		Record record = getById(id);
		if (record == null || record.getId() == null) {
			return ApiResponse.error("记录不存在");
		}
		return ApiResponse.success(record);
	}
}
