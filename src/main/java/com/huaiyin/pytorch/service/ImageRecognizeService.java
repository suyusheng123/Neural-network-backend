package com.huaiyin.pytorch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.ModelForm;
import com.huaiyin.pytorch.entity.Record;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName:ImageRecognizeService
 * Package:com.huaiyin.pytorch.service
 * Description:
 * 图像识别
 * @Author 卜翔威
 * @Create 2024/5/1 13:38
 * @Version 1.0
 */
public interface ImageRecognizeService extends IService<Record>{
	/**
	 * 图片识别
	 * @param file
	 * @return
	 */
	ApiResponse<Record> recognize(ModelForm file, HttpServletRequest request);

	ApiResponse<Page> list(int page, int pageSize,HttpServletRequest request);

	ApiResponse<Record> getRecordById(Long id);
}
