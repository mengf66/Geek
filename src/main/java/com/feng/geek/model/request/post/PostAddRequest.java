package com.feng.geek.model.request.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class PostAddRequest {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建用户Id
     */
    private Long createId;


    /**
     * 创建时间
     */
    private Date createTime;
}
