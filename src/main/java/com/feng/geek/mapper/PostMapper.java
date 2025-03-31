package com.feng.geek.mapper;

import com.feng.geek.model.domain.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【post】的数据库操作Mapper
* @createDate 2025-03-30 21:25:44
* @Entity com.feng.geek.model.domain.Post
*/
@Mapper
public interface PostMapper extends BaseMapper<Post> {

}




