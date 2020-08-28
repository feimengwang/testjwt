package test.jwt.demo.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import test.jwt.demo.common.Result;

/**
 * 全局异常处理器
 * @author pyy
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleOtherException(Exception e){
        // 打印异常信息
        return  Result.FAIL(e.getMessage());
    }

}
