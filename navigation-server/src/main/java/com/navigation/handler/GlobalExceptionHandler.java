package com.navigation.handler;

import com.navigation.exception.BaseException;
import com.navigation.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {



    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex){
        log.error(ex.getMessage());
        return Result.error(ex.getMessage());
    }

}
