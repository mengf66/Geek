package com.feng.geek.judge.codesandbox;

import com.feng.geek.judge.codesandbox.model.ExecuteCodeRequest;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public List<ExecuteCodeResponse> executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        List<ExecuteCodeResponse> executeCodeResponses = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponses.toString());
        return executeCodeResponses;
    }
}
