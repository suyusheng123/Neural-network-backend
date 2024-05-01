package com.huaiyin.pytorch.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:LoginIntercepter
 * Package:com.huaiyin.pytorch.utils
 * Description:
 *
 * @Author 卜翔威
 * @Create 2024/4/26 19:37
 * @Version 1.0
 */
public class LoginIntercepter implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response, Object handler) throws Exception {
		// 判断当前用户是否登陆(从session里面拿用户信息)
		Object user = request.getSession().getAttribute("user");
		if (user == null){
			// 没有用户信息，拦截
			response.setStatus(401);
			return false;
		}
		// 将查询到的用户信息存放到ThreadLocal中,方便其他请求来获取用户的信息
		UserHolder.set((Long)user);
		// 放行
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		 UserHolder.remove();
	}
}
