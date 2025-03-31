package com.feng.geek.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 内容
     */
    private String content;

    /**
     * 回复数
     */
    private Long commentNum;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}