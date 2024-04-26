package com.huaiyin.pytorch.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.dto.form.UserDTORegisterForm;
import com.huaiyin.pytorch.entity.User;
import com.huaiyin.pytorch.mapper.UserMapper;
import com.huaiyin.pytorch.service.UserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	/**
	 * 用户登录
	 * @param request
	 * @param user
	 * @return
	 */
	@Override
	public ApiResponse<String> login(HttpServletRequest request, UserDTOLoginForm user) {
		//1、将页面提交的密码password进行md5加密处理
		String password = user.getPassword();
		password = DigestUtils.md5DigestAsHex(password.getBytes());

		//2、根据页面提交的phone查询数据库
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getPhone,user.getPhone());
		User newUser = getOne(queryWrapper);

		//3、如果没有查询到则返回登录失败结果
		if(newUser == null || !newUser.getPassword().equals(password)){
			return ApiResponse.error("登录失败");
		}
		//4、登录成功，将员工id存入Session并返回登录成功结果
		request.getSession().setAttribute("User",newUser.getId());
		return ApiResponse.success("登陆成功");
	}
	/**
	 * 用户注册
	 * @param userForm
	 * @return
	 */
	@Override
	public ApiResponse<User> register(UserDTORegisterForm userForm) {
		// 根据手机号去数据库查询用户是否存在
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(User::getPhone,userForm.getPhone());
		User exitUser = getOne(queryWrapper);
		if(exitUser != null){
			return ApiResponse.error("用户已存在");
		}
		User user = new User();
		user.setPhone(userForm.getPhone());
		user.setPassword(DigestUtils.md5DigestAsHex(userForm.getPassword().getBytes()));
		save(user);
		return ApiResponse.success(user);
	}
}
