package com.feng.geek.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.request.UserLoginRequest;
import com.feng.geek.model.request.UserRegisterRequest;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     *
     * @param userRegister
     * @return
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody UserRegisterRequest userRegister) {
        if(userRegister == null) {
            return R.failed("传参失败");
        }
        return userService.register(userRegister);
    }

    /**
     * 用户登录
     *
     * @param userLogin
     * @return
     */
    @PostMapping("/login")
    public R<SafetyUser> login(@RequestBody UserLoginRequest userLogin, HttpServletRequest request) {
        if(userLogin == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求数据为空");
        }
        return userService.login(userLogin, request);
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        if(request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        return userService.logout(request);
    }

    /**
     * 查看个人信息
     *
     * @param request
     * @return
     */
    @GetMapping("/show")
    public R<SafetyUser> show(HttpServletRequest request) {
        return userService.show(request);
    }
}
