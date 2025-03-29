package com.feng.geek.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.feng.geek.common.ErrorCode;
import com.feng.geek.exception.BusinessException;
import com.feng.geek.judge.codesandbox.CodeSandbox;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeRequest;
import com.feng.geek.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    @Override
    public List<ExecuteCodeResponse> executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_SECRET, AUTH_REQUEST_HEADER)
                .body(json)
                .execute()
                .body();
        if(StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandBox error, message = " + responseStr);
        }
        System.out.println(responseStr);
        return JSONUtil.toList(responseStr, ExecuteCodeResponse.class);
    }
}
