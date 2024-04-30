package com.huaiyin.pytorch.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	// todo 图像识别
}
