package com.feng.geek.service;

import com.feng.geek.model.domain.CommentThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.geek.model.response.SafetyUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Lenovo
* @description 针对表【comment_thumb(评论点赞)】的数据库操作Service
* @createDate 2025-04-01 21:20:33
*/
@Service
public interface CommentThumbService extends IService<CommentThumb> {

    int doCommentThumb(long commentId, SafetyUser loginUser);

    @Transactional(rollbackFor = Exception.class)
    int doCommentThumbInner(long userId, long commentId);
}
