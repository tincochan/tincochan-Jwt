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
     * ????????????
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
        //???????????????
        http.rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(600)
                .userDetailsService(userDetailsService);
        //????????????????????????
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        //??????csrf??????
        http.csrf().disable();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * ???????????????URL
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/");
    }

    /**
     * ?????????????????????????????????????????????????????????
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
     * @return HideUserNotFoundExceptions(false)?????????UsernameNotFoundException????????????BadCredentialsException????????????
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

    //??????spirngsecurity?????????????????????????????????
    //JdbcTokenRepositoryImpl??????????????????????????????
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        //??????????????????????????????????????????????????????????????????????????????
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //???????????????
        tokenRepository.setDataSource(dataSource);
        //?????????????????????????????????????????????????????????????????????(???????????????????????????
        //?????????????????????????????????????????????????????????
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }
}
