package com.feng.geek.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.User;
import com.feng.geek.model.request.user.*;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.feng.geek.constant.UserConstant.USER_LOGIN_STATE;

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

    /**
     * 修改个人信息(不包含对密码等关键信息的改变)
     *
     * @param userUpdateRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/update")
    public R<String> update(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if(userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SafetyUser user = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        User loginUser = BeanUtil.copyProperties(user, User.class);
        boolean success = userService.updateUser(userUpdateRequest, loginUser);
        if(success) {
            Long userId = loginUser.getId();
            loginUser = userService.getById(userId);
            user = BeanUtil.copyProperties(loginUser, SafetyUser.class);
            request.getSession().removeAttribute(USER_LOGIN_STATE);
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            return R.ok("修改成功");
        } else {
            return R.failed("修改失败");
        }
    }

    /**
     * 修改密码
     *
     * @param userSecretRequest
     * @param request
     * @return
     */
    @PostMapping("/update/private")
    public R<String> updatePrivate(@RequestBody UserSecretRequest userSecretRequest, HttpServletRequest request) {
        if(userSecretRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SafetyUser user = (SafetyUser) request.getSession().getAttribute(USER_LOGIN_STATE);
        User loginUser = BeanUtil.copyProperties(user, User.class);
        loginUser = userService.updateUserSecret(userSecretRequest, loginUser);
        if(loginUser != null) {
            user = BeanUtil.copyProperties(loginUser, SafetyUser.class);
            request.getSession().removeAttribute(USER_LOGIN_STATE);
            request.getSession().setAttribute(USER_LOGIN_STATE, user);
            return R.ok("修改成功");
        } else {
            return R.failed("修改失败");
        }
    }

    /**
     * 更改用户权限（仅社长）
     *
     * @param userRoleRequest
     * @param request
     * @return
     */
    @PostMapping("/update/role")
    public R<Boolean> updateRole(@RequestBody UserRoleRequest userRoleRequest, HttpServletRequest request) {
        if(userRoleRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean boss = userService.isBoss(request);
        if(!boss) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户不是社长");
        }
        SafetyUser loginUser = userService.getLoginUser(request);
        //社长不能更改自己的权限
        if(userRoleRequest.getId() == loginUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "社长不能更改自己的权限");
        }
        return R.ok(userService.updateRole(userRoleRequest));
    }
}
