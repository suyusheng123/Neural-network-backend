package com.huaiyin.pytorch.controller;

import com.huaiyin.pytorch.common.dto.ApiResponse;
import com.huaiyin.pytorch.dto.form.UserDTOLoginForm;
import com.huaiyin.pytorch.dto.form.UserDTORegisterForm;
import com.huaiyin.pytorch.entity.User;
import com.huaiyin.pytorch.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "用户登陆注册模块")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 员工登录
     * @param request
     * @param User
     * @return
     */
    @ApiOperation("用户登陆")
    @PostMapping("/login")
    // todo RequestBody和Validated注解使用失效
    public ApiResponse<String> login(HttpServletRequest request,
                                       @RequestBody UserDTOLoginForm user){
         return userService.login(request,user);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @ApiOperation("用户退出模块")
    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("User");
        return ApiResponse.success("退出成功");
    }

    /**
     * 注册
     * @param userForm
     * @return
     */
    @ApiOperation("用户注册")
    @GetMapping("/register")
    public ApiResponse<User> register(@Validated @ModelAttribute UserDTORegisterForm userForm){
       return userService.register(userForm);
    }
}
