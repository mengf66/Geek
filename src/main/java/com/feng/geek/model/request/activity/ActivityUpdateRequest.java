package com.feng.geek.model.request.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityUpdateRequest {

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动地点
     */
    private String poistion;

    /**
     * 参加人数
     */
    private Long takeNum;
}
