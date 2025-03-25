package com.feng.geek.judge.codesandbox;

import com.feng.ojsystem.judge.codesandbox.model.ExecuteCodeRequest;
import com.feng.ojsystem.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
