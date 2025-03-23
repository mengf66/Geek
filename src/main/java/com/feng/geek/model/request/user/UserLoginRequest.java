package com.feng.geek.model.request.user;

import lombok.Data;

@Data
public class UserLoginRequest {
    /**
     * 密码
     */
    private String userPassword;

    /**
     * 邮箱
     */
    private String email;
}
