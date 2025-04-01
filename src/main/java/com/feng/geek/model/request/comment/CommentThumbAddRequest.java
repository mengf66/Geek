package com.feng.geek.model.request.comment;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentThumbAddRequest implements Serializable {

    /**
     * 帖子 id
     */
    private Long commentId;

    private static final long serialVersionUID = 1L;
}