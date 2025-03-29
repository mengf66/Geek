package com.feng.geek.judge.codesandbox.model;

import com.feng.geek.enums.JudgeInfoMessageEnum;
import lombok.Data;

@Data
public class Answer {
    private JudgeInfoMessageEnum message;

    private String input;
}
