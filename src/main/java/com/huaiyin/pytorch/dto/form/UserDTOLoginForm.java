package com.huaiyin.pytorch.dto.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * ClassName:UserDTOForm
 * Package:com.huaiyin.pytorch.dto.form
 * Description:
 * 校验前端传过来的参数
 * @Author 卜翔威
 * @Create 2024/4/26 18:43
 * @Version 1.0
 */
@Data
public class UserDTOLoginForm {

	@ApiModelProperty(value = "手机号")
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^1\\d{10}$",message = "手机号格式错误！")
	private String phone;

	@ApiModelProperty(value = "密码")
	@NotBlank(message = "请输入密码")
	@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{6,18}$",message = "密码长度需在6~18位字符，且必须包含字母和数字！")
	private String password;
}
