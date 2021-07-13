package com.example.tincochanjwt.filter;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.service.LoginUserDetailService;
import com.example.tincochanjwt.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    LoginUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取请求头中的token加密串（名字是Authorization，JwtTokenUtils工具类里面定义的
        String authHeader = request.getHeader("Authorization");
        //判断是否是以Bearer开头的，这是我自定义的前缀名）
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //将Bearer去掉，因为返回token时，我在token前缀加入了Bearer字符串
            String authToken = authHeader.substring("Bearer ".length());
            //解析加密串，获取用户名
            String username = JwtTokenUtil.parseTokenToUsername(authToken);
            //看当前SecurityContext中是否有Authentication实例（前面已经介绍过）
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //如果Authentication没有当前用户的信息，然后从数据库中查询出相应的信息
                User user = (User) userDetailsService.loadUserByUsername(username);
                if (user != null) {
                    //封装到UsernamePasswordAuthenticationToken令牌类中
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //将用户信息放到Authenticatin实例中进行存储
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

