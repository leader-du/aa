package com.ssvet.approval.config.exception;

import com.ssvet.approval.utils.exception.ApprovalException;
import com.ssvet.approval.utils.resp.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Programmer_Liu.
 * @since 2020/8/25 10:15
 */
@ControllerAdvice
@Slf4j
public class ApprovalExceptionHandler {
    //声明要捕获的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult defultExcepitonHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        if(e instanceof ApprovalException) {
            log.error("业务异常："+e.getMessage());
            ApprovalException approvalException = (ApprovalException)e;
            return CommonResult.failed(approvalException.getMessage());
        }
        log.error("未知异常："+e.getMessage());
        //未知错误
        return CommonResult.failed("系统异常，请稍后再试或者联系管理员");
    }

}
