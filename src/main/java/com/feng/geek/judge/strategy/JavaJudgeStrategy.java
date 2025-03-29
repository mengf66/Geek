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
            if (memory > 525L) {
                answer.setInput(judgeCase.getIntput());
                answer.setMessage(JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED);
                answers.add(answer);
                break;
            }
            judgeInfo.setMemory(memory);
            judgeInfo.setTime(time);
            String output = executeCodeResponse.getOutput();

            if(!output.equals(judgeCase.getOutput())) {
                answer.setInput(judgeCase.getIntput());
                answer.setMessage(JudgeInfoMessageEnum.WRONG_ANSWER);
            }
        }
        JudgeInfo judgeInfoResponse = new JudgeInfo();
//        judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//        judgeInfoResponse.setMemory(memory);
//        judgeInfoResponse.setTime(time);
//        List<String> outputList = new ArrayList<>();


        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
//        if(outputList.size() != inputList.size()) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
//            judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//            return judgeInfoResponse;
//        }

//        for(int i = 0; i < judgeCaseList.size(); i++) {
//            JudgeCase judgeCase = judgeCaseList.get(i);
//            if(!judgeCase.getOutput().equals(outputList.get(i))) {
//                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
//                judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//                return judgeInfoResponse;
//            }
//        }
//        String judgeConfigStr = question.getJudgeConfig();
//        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
//        Long needMemoryLimit = judgeConfig.getMemoryLimit();
//        Long needTimeLimit = judgeConfig.getTimeLimit();
//        if(memory > needMemoryLimit) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
//            judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//            return judgeInfoResponse;
//        }
//        long JAVA_PROGRAM_TIME_COST = 10000L;
//        if((time - JAVA_PROGRAM_TIME_COST) > needTimeLimit) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
//            judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//            return judgeInfoResponse;
//        }
//        judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
//        judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
        return judgeInfoResponse;
    }
}
