package com.feng.geek.mapper;

import com.feng.geek.model.domain.CommentThumb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【comment_thumb(评论点赞)】的数据库操作Mapper
* @createDate 2025-04-01 21:20:33
* @Entity com.feng.geek.model.domain.CommentThumb
*/
@Mapper
public interface CommentThumbMapper extends BaseMapper<CommentThumb> {

}




