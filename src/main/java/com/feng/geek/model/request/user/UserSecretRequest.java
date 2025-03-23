package com.feng.geek.model.request.user;

import lombok.Data;

@Data
public class UserSecretRequest {

    /**
     * 确认密码
     */
    private String userAgainPassword;

    /**
     * 密码
     */
    private String userPassword;

}
