package com.huaiyin.pytorch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.dto.form.UserDTORegisterForm;
import com.huaiyin.pytorch.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

	ApiResponse<String> login(HttpServletRequest request, UserDTOLoginForm user);

	ApiResponse<User> register(UserDTORegisterForm userForm);
}
