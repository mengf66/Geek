package com.feng.geek.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feng.geek.model.domain.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.geek.model.request.question.QuestionAddRequest;
import com.feng.geek.model.request.question.QuestionQueryRequest;
import com.feng.geek.model.request.question.QuestionUpdateRequest;
import com.feng.geek.model.response.SafetyUser;
import com.feng.geek.model.vo.QuestionVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【question】的数据库操作Service
* @createDate 2025-03-23 22:18:12
*/
@Service
public interface QuestionService extends IService<Question> {

    R<Long> saveQuestion(QuestionAddRequest questionAddRequest, SafetyUser loginUser);

    void validQuestion(Question question, boolean add);

    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest);

    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
