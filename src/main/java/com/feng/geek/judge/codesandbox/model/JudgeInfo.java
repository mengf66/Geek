package com.feng.geek.judge.codesandbox.model;

import lombok.Data;

import java.util.List;

@Data
public class JudgeInfo {
    private List<Answer> answers;
    private Long memory;
    private Long time;
}
