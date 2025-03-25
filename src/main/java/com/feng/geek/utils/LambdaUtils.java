package com.feng.geek.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class LambdaUtils {

    /**
     * 将 Method 对象转换为 SFunction
     *
     * @param method 要转换的 Method 对象
     * @param <T>    实体类类型
     * @param <R>    返回值类型
     * @return 对应的 SFunction
     */
    public static <T, R> SFunction<T, R> getLambdaMethod(Method method) {
        try {
            // 获取方法名
            String methodName = method.getName();
            // 获取方法返回类型
            Class<R> returnType = (Class<R>) method.getReturnType();
            // 获取实体类类型
            Class<T> clazz = (Class<T>) method.getDeclaringClass();

            // 使用 MethodHandles 生成 Lambda 表达式
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle methodHandle = lookup.unreflect(method);

            // 生成 Lambda 表达式
            return (SFunction<T, R>) LambdaMetafactoryUtils.createLambda(
                    clazz,
                    methodName,
                    returnType,
                    methodHandle
            );
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create SFunction from Method", e);
        }
    }
}