package com.feng.geek.model.request.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserUpdateRequest {
    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 个人介绍
     */
    private String introduction;

    /**
     * 标签列表
     */
    private String tags;
}
