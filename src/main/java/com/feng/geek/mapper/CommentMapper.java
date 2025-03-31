package com.feng.geek.mapper;

import com.feng.geek.model.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2025-03-30 21:25:57
* @Entity com.feng.geek.model.domain.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




