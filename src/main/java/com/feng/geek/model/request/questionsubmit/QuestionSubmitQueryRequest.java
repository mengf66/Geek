package com.feng.geek.model.request.questionsubmit;

import com.feng.geek.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     *编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目id
     */
    private Long questionId;


    /**
     * 用户 Id
     */
    private Long userId;

    public static final long serialVersionUID = 1L;
}