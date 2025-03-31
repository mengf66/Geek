package com.feng.geek.judge.strategy;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.feng.geek.enums.JudgeInfoMessageEnum;
import com.feng.geek.judge.codesandbox.model.Answer;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;
import com.feng.geek.judge.codesandbox.model.JudgeInfo;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.request.question.JudgeCase;
import com.feng.geek.model.request.question.JudgeConfig;


import java.util.ArrayList;
import java.util.List;

public class JavaJudgeStrategy implements JudgeStrategy{
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

//        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        List<String> inputList = judgeContext.getInputList();
        List<ExecuteCodeResponse> executeCodeResponses = judgeContext.getExecuteCodeResponses();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();
//        Long memory = judgeInfo.getMemory();
//        Long time = judgeInfo.getTime();
        long maxTime = 0;
        long maxMemory = 0;
        JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);
        final long timeLimit = judgeConfig.getTimeLimit();
        final long memoryLimit = judgeConfig.getMemoryLimit();
//        1.先判断长度和输入是否相同，不同则报错
        if(inputList.size() == executeCodeResponses.size()) {
            return null;
        }
        List<JudgeCase> judgeCases = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        JudgeInfo judgeInfo = new JudgeInfo();
        List<Answer> answers = new ArrayList<>();
        for(int i = 0; i < executeCodeResponses.size(); i++) {
            Answer answer = new Answer();
            ExecuteCodeResponse executeCodeResponse = executeCodeResponses.get(i);
            JudgeCase judgeCase = judgeCases.get(i);
            Long memory = executeCodeResponse.getJudgeInfo().getMemory();
            Long time = executeCodeResponse.getJudgeInfo().getTime();
//          2.判断每个返回的时间和内存，有溢出则保存在对应的答案配置中
            if (memory > memoryLimit) {
                answer.setInput(judgeCase.getIntput());
                answer.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED);
                answers.add(answer);
                break;
            }
            if(time > timeLimit) {
                answer.setInput(judgeCase.getIntput());
                answer.setMessage(JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED);
                answers.add(answer);
                break;
            }
//            3.若时间内存无溢出，保存最大的时间和内存
            maxTime = Math.max(maxTime, time);
            maxMemory = Math.max(maxMemory, maxMemory);

//            4.比叫答案和返回结果
            String output = executeCodeResponse.getOutput();
            if(!output.equals(judgeCase.getOutput())) {
                answer.setInput(judgeCase.getIntput());
                answer.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER);
                answers.add(answer);
            }
        }
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfo.setAnswers(answers);
        judgeInfoResponse.setMemory(maxMemory);
        judgeInfoResponse.setTime(maxTime);
        return judgeInfoResponse;
    }
}
