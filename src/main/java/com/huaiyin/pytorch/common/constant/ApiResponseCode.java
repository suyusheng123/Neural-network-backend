package com.huaiyin.pytorch.common.constant;

/**
 * ClassName:ApiResponseCode
 * Package:com.bage
 * Description:
 * 状态信息码
 * @Author 卜翔威
 * @Create 2024/4/14 15:23
 * @Version 1.0
 */
public enum ApiResponseCode {
	/**
	 * 成功
	 */
	SUCCESS(0, "success"),

	/**
	 * 参数错误
	 */
	PARAMETER_INVALID(100, "parameter_invalid"),


	/**
	 * 业务错误
	 */
	BUSINESS_ERROR(200, "业务错误"),

	/**
	 * 登录错误
	 */
	LOGIN_ERROR(201, "登录失败"),

	/**
	 * 账号或密码错误
	 */
	ACCOUNT_PASSWORD_ERROR(202, "账号或密码错误"),

	/**
	 * 账号错误
	 */
	ACCOUNT_ERROR(203, "账号错误"),

	/**
	 * 服务异常
	 */
	SERVICE_ERROR(500, "service_error");

	private Integer code;

	private String message;

	private ApiResponseCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
