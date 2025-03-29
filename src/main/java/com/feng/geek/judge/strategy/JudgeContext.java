package com.feng.geek.judge.strategy;


import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;
import com.feng.geek.judge.codesandbox.model.JudgeInfo;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.domain.QuestionSubmit;
import com.feng.geek.model.request.question.JudgeCase;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {

    private List<String> inputList;

    private List<ExecuteCodeResponse> executeCodeResponses;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
