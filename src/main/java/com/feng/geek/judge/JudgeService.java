package com.feng.geek.judge;


import com.feng.geek.model.domain.QuestionSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     *判题
     * @param questionSubmitId
     * @return
     */
    public QuestionSubmit doJudge(long questionSubmitId);
}
