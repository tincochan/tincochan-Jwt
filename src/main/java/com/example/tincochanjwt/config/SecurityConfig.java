package com.example.tincochanjwt.config;

import com.example.tincochanjwt.filter.AuthenticationFilter;
import com.example.tincochanjwt.handle.*;

import com.example.tincochanjwt.service.LoginUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private LoginUserDetailService userDetailsService;
    @Autowired
    private AjaxAccessDeniedHandler deniedHandler;
    @Autowired
    private AjaxAuthenticationEntryPoint ajaxAuthenticationEntryPoint;
    @Autowired
    private AjaxAuthenticationSuccessHandler successHandler;
    @Autowired
    private AjaxAuthenticationFailureHandler failureHandler;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AuthenticationFilter authenticationFilter;
    @Autowired
    private AjaxLogoutSuccessHandler logoutSuccessHandler;
    /**
     * 拦截策略
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().and()
                .formLogin().loginPage("/login.html")
                .loginProcessingUrl("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .authorizeRequests().antMatchers("/login.html","/register.html","/register","/js/**").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(deniedHandler)
                .authenticationEntryPoint(ajaxAuthenticationEntryPoint)
                .and().authorizeRequests()
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //设置记住我
        http.rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(600)
                .userDetailsService(userDetailsService);
        //配置退出登录操作
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        //关闭csrf防护
        http.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置忽略的URL
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/");
    }

    /**
     * 拦截后需要使用自定义的类和加密解密方式
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());

    }


    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     *
     * @return HideUserNotFoundExceptions(false)，否则UsernameNotFoundException异常会被BadCredentialsException异常覆盖
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

    //这是spirngsecurity记住我功能需要加入代码
    //JdbcTokenRepositoryImpl是将其保存到数据库中
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        //这个是记住我保存到数据库的类，当然还有保存到内存的类
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        tokenRepository.setDataSource(dataSource);
        //这个会在第一次使用记住我时在数据库中创建一张表(用户、过期时间等）
        //在第二次一定要将其注释掉，否者会报错。
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
