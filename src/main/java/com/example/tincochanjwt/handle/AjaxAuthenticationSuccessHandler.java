package com.example.tincochanjwt.handle;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.utils.JwtTokenUtil;
import com.example.tincochanjwt.utils.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:    认证成功Handler处理类
 */
@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private String url;

    public AjaxAuthenticationSuccessHandler() {
    }

    public AjaxAuthenticationSuccessHandler(String url) {
        this.url = url;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String token = JwtTokenUtil.createToken(user.getUsername(), String.valueOf(user.getAuthorities()));
        //设置编码,如果不设置会乱码
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        //设置返回的token 带有Bearer的前缀字符串
        response.setHeader("Authorization",  JwtTokenUtil.TOKEN_PREFIX+token);
        response.getWriter().write(new ResponseBody<User>(200,"登录成功",user).toString());
    }
}
