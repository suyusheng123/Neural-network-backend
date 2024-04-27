package com.huaiyin.pytorch.dto.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * ClassName:UserDTORegisterForm
 * Package:com.huaiyin.pytorch.dto.form
 * Description:
 * 注册传入的参数
 * @Author 卜翔威
 * @Create 2024/4/26 19:15
 * @Version 1.0
 */
@Data
public class UserDTORegisterForm {

	@ApiModelProperty(value = "手机号")
	 @NotBlank(message = "手机号不能为空")
	 @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误！")
	private String phone;

	@ApiModelProperty(value = "密码")
	 @NotBlank(message = "请输入密码")
	 @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{6,18}$", message = "密码长度需在6~18位字符，且必须包含字母和数字！")
	private String password;

	@ApiModelProperty(value = "确认密码")
	@NotBlank(message = "请输入确认密码")
	@Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{6,18}$", message = "密码长度需在6~18位字符，且必须包含字母和数字！")
	private String confirmPassword;
}
