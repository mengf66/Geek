package com.feng.geek.judge.codesandbox;


import com.feng.geek.judge.codesandbox.model.ExecuteCodeRequest;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;

import java.util.List;

public interface CodeSandbox {

    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    List<ExecuteCodeResponse> executeCode(ExecuteCodeRequest executeCodeRequest);
}
