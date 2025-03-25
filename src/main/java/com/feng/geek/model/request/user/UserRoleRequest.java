package com.feng.geek.model.request.user;

import lombok.Data;

@Data
public class UserRoleRequest {
    //被更改用户的Id
    long id;
    //更改后用户的权限
    int role;
}
