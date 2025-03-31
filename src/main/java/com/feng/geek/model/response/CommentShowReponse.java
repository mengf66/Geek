package com.feng.geek.model.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class CommentShowReponse {
    /**
     *
     */
    private Long id;

    /**
     * 发布用户ID
     */
    private Long userId;

    /**
     * 评论帖子Id
     */
    private Long postId;

    /**
     * 点赞数量
     */
    private Long thumbNum;

    /**
     *
     */
    private Date createTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复数
     */
    private Long commentNum;
}
