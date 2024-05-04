package com.huaiyin.pytorch.service;

import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:CommonService
 * Package:com.huaiyin.pytorch.service
 * Description:
 * 文件上传服务
 * @Author 卜翔威
 * @Create 2024/5/1 16:52
 * @Version 1.0
 */
public interface CommonService {
	/**
	 * 文件上传
	 * @param file
	 * @return
	 */
	ApiResponse<ImageDTO> upload(MultipartFile file);

	void download(String name, HttpServletResponse response);

	void recognize(String name, HttpServletResponse response);
}
