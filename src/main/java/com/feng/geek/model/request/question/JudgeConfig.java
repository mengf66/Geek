package com.feng.geek.model.request.question;

import lombok.Data;

@Data
public class JudgeConfig {
    private long timeLimit;
    private long memoryLimit;
    private long stackLimit;

}
