package com.example.tincochanjwt.exception.controllerException;

import com.example.tincochanjwt.exception.RegisterUsernameHasBeenExists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ControllerHandlerExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ControllerHandlerExceptionAdvice.class);
    /**
     * 拦截表单参数异常处理
     * @param exception
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BindException.class})
    public String bindException(BindException exception, HttpServletRequest request) {
        logger.info("表单拦截校验处理=====>");
        logger.info("表单数据======>"+request.getContentType());
        BindingResult bindingResult = exception.getBindingResult();
        return Objects.requireNonNull(bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    public String handler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.info("RestFul 请求发生异常........");
        if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            logger.info("状态值不是200,正准备修改为200");
            response.setStatus(HttpStatus.OK.value());
        }
        if (e instanceof NullPointerException) {
            logger.error("发生了空指针异常======》",e.getMessage());
            return "空指针异常";
        } else if (e instanceof IllegalArgumentException) {
            logger.error("请求参数不匹配异常======>",e.getMessage());
            return "请求参数不匹配";
        } else if (e instanceof SQLException) {
            logger.error("数据库访问异常======>",e.getMessage());
            return "数据库访问异常";
        } else if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            logger.error("表单校验异常",bindingResult.getFieldError().getDefaultMessage());
            return Objects.requireNonNull(bindingResult.getFieldError().getDefaultMessage());
        } else if (e instanceof RegisterUsernameHasBeenExists) {
            logger.info("用户名已经存在");
            return "用户名已经存在";
        } else {
            logger.error("未知异常",e.getMessage());
            return "服务器端未知异常,请去检查!";
        }
    }
}
