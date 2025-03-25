package com.feng.geek.exception;

import com.baomidou.mybatisplus.extension.api.R;

import com.feng.geek.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return R.failed(e.getCode() + " " + e.getMessage() + " " + e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public R runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return R.failed(ErrorCode.SYSTEM_ERROR + e.getMessage() + "");
    }
}
