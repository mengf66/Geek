package com.feng.geek.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.invoke.*;
import java.util.function.Function;

public class LambdaMetafactoryUtils {

    /**
     * 使用 LambdaMetafactory 创建 SFunction
     *
     * @param clazz        实体类类型
     * @param methodName   方法名
     * @param returnType   返回值类型
     * @param methodHandle 方法句柄
     * @param <T>          实体类类型
     * @param <R>          返回值类型
     * @return 对应的 SFunction
     * @throws Throwable 如果创建过程中发生错误
     */
    public static <T, R> SFunction<T, R> createLambda(Class<T> clazz, String methodName, Class<R> returnType, MethodHandle methodHandle) throws Throwable {
        // 定义 Lambda 的签名
        MethodType invokedType = MethodType.methodType(SFunction.class);
        // 定义要调用的方法类型
        MethodType methodType = MethodType.methodType(returnType, clazz);
        // 使用 LambdaMetafactory 生成 Lambda
        CallSite site = LambdaMetafactory.metafactory(
                MethodHandles.lookup(),
                "apply",
                invokedType,
                methodType.generic(),
                methodHandle,
                methodType
        );
        // 获取工厂方法
        MethodHandle factory = site.getTarget();
        // 创建 Function 对象
        Function<T, R> function = (Function<T, R>) factory.invoke();
        // 转换为 SFunction
        return (SFunction<T, R>) function;
    }
}