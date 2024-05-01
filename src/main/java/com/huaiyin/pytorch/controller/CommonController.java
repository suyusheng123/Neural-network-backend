package com.huaiyin.pytorch.controller;

import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:CommonController
 * Package:com.huaiyin.pytorch.controller
 * Description:
 * 文件上传下载接口
 * @Author 卜翔威
 * @Create 2024/5/1 16:45
 * @Version 1.0
 */

@Api(tags = "文件上传下载")
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
	@Resource
	private CommonService commonService;

	@ApiOperation("文件上传")
	@PostMapping("/upload")
	public ApiResponse<String> upload(@RequestParam("file") MultipartFile file){
		return commonService.upload(file);
	}

	@ApiOperation("原始文件下载")
	@GetMapping("/download")
	public void download(@RequestParam("file") String name, HttpServletResponse response){
		commonService.download(name,response);
	}

	@ApiOperation("识别文件下载")
	@GetMapping("/recognize")
	public void recognize(@RequestParam("file") String name, HttpServletResponse response){
		commonService.recognize(name,response);
	}
}
