package com.feng.geek.judge.codesandbox;


import com.feng.geek.judge.codesandbox.impl.RemoteCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱实例
     *
     * @param type
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        switch(type) {
            case "remote":
                return new RemoteCodeSandbox();
            default:
                return new RemoteCodeSandbox();
        }
    }
}
