package com.feng.geek.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.api.R;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.model.domain.Comment;
import com.feng.geek.model.request.comment.CommentAddRequest;
import com.feng.geek.model.request.comment.CommentThumbAddRequest;
import com.feng.geek.model.request.postthumb.PostThumbAddRequest;
import com.feng.geek.model.response.CommentShowReponse;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.service.CommentService;
import com.feng.geek.service.CommentThumbService;
import com.feng.geek.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private CommentThumbService commentThumbService;

    @Resource
    private UserService userService;

    /**
     * 评论添加
     *
     * @param commentAddRequest
     * @return
     */
    @PostMapping("/add")
    public R<Long> addComment(@RequestBody CommentAddRequest commentAddRequest) {
        if(commentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = BeanUtil.copyProperties(commentAddRequest, Comment.class);
        boolean save = commentService.save(comment);
        if(!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return R.ok(comment.getId());
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @param request
     * @return
     */
    @GetMapping("/delete/{commentId}")
    public R<String> deleteComment(@PathVariable long commentId, HttpServletRequest request) {
        if(Long.valueOf(commentId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //只有管理人员和本人可以删评论
        boolean auth = userService.isAuth(request);
        SafetyUser loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        Comment comment = commentService.getById(commentId);
        Long createId = comment.getUserId();
        if((auth && (createId.longValue() == loginUserId))) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return R.ok("删除成功");
    }

    /**
     * 根据评论Id查询评论
     * 
     * @param commentId
     * @return
     */
    @GetMapping("/show/{commentId}")
    public R<CommentShowReponse> getComment(@PathVariable long commentId) {
        if(Long.valueOf(commentId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Comment comment = commentService.getById(commentId);
        CommentShowReponse commentShowReponse = BeanUtil.copyProperties(comment, CommentShowReponse.class);
        return R.ok(commentShowReponse);
    }

    /**
     * 点赞 / 取消点赞
     *
     * @param commentThumbAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/thumb")
    public R<Integer> doThumb(@RequestBody CommentThumbAddRequest commentThumbAddRequest,
                              HttpServletRequest request) {
        if (commentThumbAddRequest == null || commentThumbAddRequest.getCommentId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final SafetyUser loginUser = userService.getLoginUser(request);
        long commentId = commentThumbAddRequest.getCommentId();
        int result = commentThumbService.doCommentThumb(commentId, loginUser);
        return R.ok(result);
    }

}
