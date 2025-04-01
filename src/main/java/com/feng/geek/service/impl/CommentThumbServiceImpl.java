package com.feng.geek.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.CommentThumb;
import com.feng.geek.model.domain.Comment;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.CommentThumbService;
import com.feng.geek.mapper.CommentThumbMapper;
import com.feng.geek.service.CommentService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author Lenovo
* @description 针对表【comment_thumb(评论点赞)】的数据库操作Service实现
* @createDate 2025-04-01 21:20:33
*/
@Service
public class CommentThumbServiceImpl extends ServiceImpl<CommentThumbMapper, CommentThumb>
    implements CommentThumbService{

    @Resource
    private CommentService commentService;

    /**
     * 点赞
     *
     * @param commentId
     * @param loginUser
     * @return
     */
    @Override
    public int doCommentThumb(long commentId, SafetyUser loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Comment comment = commentService.getById(commentId);
        if (comment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        CommentThumbService commentThumbService = (CommentThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return commentThumbService.doCommentThumbInner(userId, commentId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param commentId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doCommentThumbInner(long userId, long commentId) {
        CommentThumb commentThumb = new CommentThumb();
        commentThumb.setUserId(userId);
        commentThumb.setCommentId(commentId);
        QueryWrapper<CommentThumb> thumbQueryWrapper = new QueryWrapper<>(commentThumb);
        CommentThumb oldCommentThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldCommentThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = commentService.update()
                        .eq("id", commentId)
                        .gt("thumbNum", 0)
                        .setSql("thumbNum = thumbNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(commentThumb);
            if (result) {
                // 点赞数 + 1
                result = commentService.update()
                        .eq("id", commentId)
                        .setSql("thumbNum = thumbNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




