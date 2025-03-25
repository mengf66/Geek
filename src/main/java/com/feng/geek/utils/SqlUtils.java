package com.feng.geek.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import java.lang.reflect.Field;
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }

    /**
     * 将字段名转换为 SFunction（Lambda 表达式）
     *
     * @param clazz     实体类
     * @param fieldName 字段名
     * @param <T>       实体类类型
     * @return SFunction
     */
    public static <T> SFunction<T, ?> getLambdaExpression(Class<T> clazz, String fieldName) {
        try {
            // 使用反射获取字段的 getter 方法
            java.lang.reflect.Method method = clazz.getMethod("get" + capitalize(fieldName));
            return (SFunction<T, ?>) LambdaUtils.getLambdaMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("No getter method found for field: " + fieldName, e);
        }
    }

    /**
     * 首字母大写
     *
     * @param str 字符串
     * @return 首字母大写后的字符串
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

