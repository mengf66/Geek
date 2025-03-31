package com.feng.geek.model.request.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class CommentAddRequest {

    /**
     * 发布用户ID
     */
    private Long userId;

    /**
     * 评论帖子Id
     */
    private Long postId;

    /**
     *
     */
    private Date createTime;

    /**
     * 内容
     */
    private String content;
}
