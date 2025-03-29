package com.feng.geek.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {

    private String output;

    private String message;

    /**
     * 0 - 等待中 1 - 判题中 2 - 成功 3 - 失败
     */
    private Integer status;

    private JudgeInfo judgeInfo;
}
