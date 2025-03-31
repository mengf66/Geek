package com.feng.geek.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.feng.geek.enums.JudgeInfoMessageEnum;
import com.feng.geek.judge.codesandbox.model.JudgeInfo;
import com.feng.geek.model.domain.Question;
import com.feng.geek.model.request.question.JudgeCase;
import com.feng.geek.model.request.question.JudgeConfig;


import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy{

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

//        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
//        List<String> inputList = judgeContext.getInputList();
//        List<String> outputList = judgeContext.getOutputList();
//        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
//        Question question = judgeContext.getQuestion();
//        Long memory = judgeInfo.getMemory();
//        Long time = judgeInfo.getTime();
//        JudgeInfo judgeInfoResponse = new JudgeInfo();
//        judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//        judgeInfoResponse.setMemory(memory);
//        judgeInfoResponse.setTime(time);
//
//
//        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.WAITING;
//        if(outputList.size() != inputList.size()) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
//            judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//            return judgeInfoResponse;
//        }
//
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
//        if(time > needTimeLimit) {
//            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
//            judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//            return judgeInfoResponse;
//        }
//        judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
//        judgeInfoResponse.setMessage(judgeInfoResponse.getMessage());
//        return judgeInfoResponse;
        return null;
    }
}
