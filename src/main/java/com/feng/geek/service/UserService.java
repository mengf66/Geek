package com.feng.geek.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.feng.geek.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.geek.model.request.user.UserLoginRequest;
import com.feng.geek.model.request.user.UserRegisterRequest;
import com.feng.geek.model.request.user.UserSecretRequest;
import com.feng.geek.model.request.user.UserUpdateRequest;
import com.feng.geek.model.response.SafetyUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Service
* @createDate 2025-03-21 21:44:10
*/
@Service
public interface UserService extends IService<User> {

    R<String> register(UserRegisterRequest userRegister);

    R<SafetyUser> login(UserLoginRequest userLogin, HttpServletRequest request);

    R<String> logout(HttpServletRequest request);

    R<SafetyUser> show(HttpServletRequest request);

    boolean updateUser(UserUpdateRequest userUpdateRequest, User loginUser);

    User updateUserSecret(UserSecretRequest userSecretRequest, User loginUser);
}
