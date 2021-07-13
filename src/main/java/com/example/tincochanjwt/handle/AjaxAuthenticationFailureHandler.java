package com.example.tincochanjwt.handle;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:    认证失败Handler处理类
 */
@Slf4j
@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private String url;

    public AjaxAuthenticationFailureHandler(){}
    public AjaxAuthenticationFailureHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String failData = "";
        if (exception instanceof AccountExpiredException) {
            failData = "账号过期";
        } else if (exception instanceof UsernameNotFoundException) {
            failData = "账号不存在";
        } else if (exception instanceof CredentialsExpiredException) {
            failData = "密码过期";
        } else if (exception instanceof DisabledException) {
            failData = "账号不可用";
        } else if (exception instanceof LockedException) {
            failData = "账号锁定";
        } else if (exception instanceof BadCredentialsException) {
            failData = "密码错误";
        } else {
            failData = "未知异常";
        }
        log.info("认证失败,"+failData);
        //设置编码格式，否则中文会乱码
        response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
        //返回统一数据
        response.getWriter().write(JSON.toJSONString(failData));
    }
}

