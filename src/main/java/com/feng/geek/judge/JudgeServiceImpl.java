package com.feng.geek.judge;

import cn.hutool.json.JSONUtil;
import com.feng.geek.common.ErrorCode;
import com.feng.geek.enums.QuestionSubmitStatusEnum;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.judge.JudgeService;

import com.feng.geek.judge.codesandbox.CodeSandbox;
import com.feng.geek.judge.codesandbox.CodeSandboxFactory;
import com.feng.geek.judge.codesandbox.CodeSandboxProxy;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeRequest;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;
import com.feng.geek.judge.codesandbox.model.JudgeInfo;
import com.feng.geek.judge.strategy.DefaultJudgeStrategy;
import com.feng.geek.judge.strategy.JudgeContext;
import com.feng.geek.judge.strategy.JudgeManager;
import com.feng.geek.judge.strategy.JudgeStrategy;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.domain.QuestionSubmit;
import com.feng.geek.model.request.question.JudgeCase;
import com.feng.geek.service.QuestionService;
import com.feng.geek.service.QuestionSubmitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

//    @Value("${codesandbox.type:example}")
    private String type = "remote";

    @Resource
    private JudgeManager judgeManager;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if(questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if(question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        if(!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if(!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getIntput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        List<ExecuteCodeResponse> executeCodeResponses = codeSandbox.executeCode(executeCodeRequest);
//        List<String> outputList = executeCodeResponse.getOutputList();
//
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setInputList(inputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        judgeContext.setExecuteCodeResponses(executeCodeResponses);
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
//
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if(!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }
}
