package com.huaiyin.pytorch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.ModelForm;
import com.huaiyin.pytorch.entity.Record;
import com.huaiyin.pytorch.service.ImageRecognizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * ClassName:ImageRecognitionController
 * Package:com.huaiyin.pytorch.controller
 * Description:
 * 图片识别类
 * @Author 卜翔威
 * @Create 2024/4/28 22:40
 * @Version 1.0
 */

@Api(tags = "图像识别")
@RestController
@Slf4j
@RequestMapping("/image")
public class ImageRecognitionController {

	@Resource
	private ImageRecognizeService imageRecognitionService;

	/**
	 * 图片识别
	 * @param file
	 * @return
	 */
	@ApiOperation(value = "图片识别")
	@PostMapping("/recognize")
	public ApiResponse<Record> recognize(@RequestBody ModelForm file, HttpServletRequest request){
		return imageRecognitionService.recognize(file,request);
	}

	/**
	 * 图片分页查询
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@ApiOperation(value = "图片分页查询")
	@GetMapping("/page")
	public ApiResponse<Page> page(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
		return imageRecognitionService.list(page,pageSize);
	}
	@ApiOperation(value = "根据id查询记录")
	@GetMapping("/{id}")
	public ApiResponse<Record> getRecordById(@PathVariable("id") Long id){
		return imageRecognitionService.getRecordById(id);
	}

}



