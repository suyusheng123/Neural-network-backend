package com.huaiyin.pytorch.common.dto;

import com.huaiyin.pytorch.common.constant.ApiResponseCode;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:ApiResponse
 * Package:com.bage.common.dto
 * Description:
 * 接口返回数据格式
 *
 * @Author 卜翔威
 * @Create 2024/4/14 15:26
 * @Version 1.0
 */

@Data
public class ApiResponse<T> {
	/**
	 * 数据字段，类型为T。此字段用于存储响应的实际数据。
	 */
	private T data;

	/**
	 * 代码字段，类型为Integer。此字段用于存储响应的状态代码。
	 * 默认情况下，它被设置为0。
	 */
	private Integer code = 0;

	/**
	 * 代码消息字段，类型为String。此字段用于存储与状态代码对应的消息。
	 */
	private String codeMessage;

	/**
	 * 错误消息字段，类型为Map<String, String>。此字段用于存储可能发生的任何错误消息。
	 */
	private Map<String, String> errorMessage;

	private String newError;

	/**
	 * 成功字段，类型为Boolean。此字段用于指示操作是否成功。
	 * 默认情况下，它被设置为true。
	 */
	private Boolean success = true;

	public static <T> ApiResponse<T> success() {
		return success(null);
	}
	public static <T> ApiResponse<T> success(String msg){
		ApiResponse<T> response = new ApiResponse<>();
		response.setData(null);
		response.setCode(ApiResponseCode.SUCCESS.getCode());
		response.setCodeMessage(msg);
		response.setSuccess(true);
		return response;
	}

	public static <T> ApiResponse<T> success(T data) {
		ApiResponse<T> response = new ApiResponse<>();
		response.setData(data);
		response.setCode(ApiResponseCode.SUCCESS.getCode());
		response.setCodeMessage(ApiResponseCode.SUCCESS.getMessage());
		response.setSuccess(true);
		return response;
	}

	public static <T> ApiResponse<T> error(Map<String, String> errors) {
		ApiResponse<T> response = new ApiResponse<>();
		response.setData(null);
		response.setCode(ApiResponseCode.SERVICE_ERROR.getCode());
		response.setCodeMessage(ApiResponseCode.SERVICE_ERROR.getMessage());
		response.setErrorMessage(errors);
		response.setSuccess(false);
		return response;
	}

	public static <T> ApiResponse<T> error(String error) {
		ApiResponse<T> response = new ApiResponse<>();
		response.setData(null);
		response.setCode(ApiResponseCode.SERVICE_ERROR.getCode());
		response.setCodeMessage(ApiResponseCode.SERVICE_ERROR.getMessage());
		response.setNewError(error);
		return response;
	}


	public ApiResponse<T> error(String msg, T data) {
		this.setData(data);
		this.setSuccess(false);
		this.setCode(ApiResponseCode.SERVICE_ERROR.getCode());
		this.setCodeMessage(ApiResponseCode.SERVICE_ERROR.getMessage());
		return this;
	}

	public ApiResponse<T> error(Integer code, Map<String, String> errors) {
		this.setCode(code);
		this.setErrorMessage(errors);
		this.setData(data);
		this.setSuccess(false);
		return this;
	}
}
