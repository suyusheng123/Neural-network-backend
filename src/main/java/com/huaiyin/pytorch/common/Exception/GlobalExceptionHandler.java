package com.huaiyin.pytorch.common.Exception;
import com.huaiyin.pytorch.common.constant.ApiResponseCode;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:GlobalExceptionHandler
 * Package:com.huaiyin.pytorch.common
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/4/26 19:43
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ApiResponse<String> handleRuntimeException(RuntimeException e) {
		log.error(e.toString(), e);
		return ApiResponse.error("服务器异常");
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("MethodArgumentNotValidException:", ex);
		ApiResponse<Object> response = new ApiResponse<>();
		Map<String, String> errors = new HashMap<>();
		ex.getFieldErrors().forEach(p -> {
			errors.put(p.getField(), p.getDefaultMessage());
		});
		response.error(ApiResponseCode.PARAMETER_INVALID.getCode(), errors);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * BindException异常处理
	 * BindException: 作用于@Validated @Valid 注解
	 * 仅对于表单提交参数进行异常处理，对于以json格式提交将会失效
	 * 只对实体参数进行校验
	 * 注：Controller类里面的方法必须加上@Validated 注解
	 *
	 * @param ex BindException异常信息
	 * @return 响应数据
	 */
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("BindException:", ex);
		// 返回响应对象
		ApiResponse<Object> apiResponse = new ApiResponse<>();
		Map<String, String> errors = new HashMap<>();
		ex.getFieldErrors().forEach(p -> {
			errors.put(p.getField(), p.getDefaultMessage());
		});
		apiResponse.error(ApiResponseCode.PARAMETER_INVALID.getCode(), errors);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}
}
