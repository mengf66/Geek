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
 * @TableName activity
 */
@TableName(value ="activity")
@Data
public class Activity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 发布者Id
     */
    private Long userId;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动地点
     */
    private String poistion;

    /**
     * 
     */
    private Date createTime;

    /**
     * 参加人数
     */
    private Long takeNum;

    /**
     * 已参加人数
     */

    private Long alTakeNum;

    /**
     * 
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}