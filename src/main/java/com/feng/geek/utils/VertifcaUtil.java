package com.feng.geek.utils;

import java.security.SecureRandom;

public class VertifcaUtil {
    /**
     * 使用SecureRandom生成指定长度的随机数字验证码
     *
     * @param length 验证码的长度
     * @return 随机生成的数字验证码
     */
    public static String getCode(int length) {
        StringBuilder code = new StringBuilder(length);
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i < length; i++) {
            int digit = secureRandom.nextInt(10); // 生成0-9之间的随机数
            code.append(digit);
        }
        return code.toString();
    }
}
