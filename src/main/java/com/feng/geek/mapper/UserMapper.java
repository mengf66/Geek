package com.feng.geek.mapper;

import com.feng.geek.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-21 21:44:10
* @Entity com.feng.geek.model.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




