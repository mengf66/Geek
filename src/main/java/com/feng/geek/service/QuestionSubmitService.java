package com.feng.geek.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.geek.model.domain.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.geek.model.request.questionsubmit.QuestionSubmitAddRequest;
import com.feng.geek.model.request.questionsubmit.QuestionSubmitQueryRequest;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.model.vo.QuestionSubmitVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author Lenovo
* @description 针对表【question_submit】的数据库操作Service
* @createDate 2025-03-23 22:18:08
*/
@Service
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, SafetyUser loginUser);

    @Transactional(rollbackFor = Exception.class)
    int doQuestionSubmitInner(long userId, long questionId);

    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, SafetyUser loginUser);

    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, SafetyUser loginUser);
}
