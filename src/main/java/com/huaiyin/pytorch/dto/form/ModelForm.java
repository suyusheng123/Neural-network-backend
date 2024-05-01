package com.huaiyin.pytorch.dto.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ClassName:ModelForm
 * Package:com.huaiyin.pytorch.dto.form
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/5/1 15:13
 * @Version 1.0
 */

@Data
public class ModelForm {

	@NotBlank(message = "图片不能为空")
	// 上传的图片文件
	private String image;
	// 模型置信区间
	private float confThreshold;
	// 模型NMS阈值
	private float nmsThreshold;
}
