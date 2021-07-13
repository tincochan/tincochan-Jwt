package com.example.tincochanjwt.service;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LoginUserDetailService implements UserDetailsService {

    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     *
     * 自定义认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.equals("")) {
            throw new RuntimeException("用户名不能为空!");
        }
        User user = loginMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不能为空!");
        }
        log.info("根据username查询的用户信息为===>"+user.toString() + "==》等待认证！");
        List<GrantedAuthority> authorities = new ArrayList<>();
//        loginMapper.findAuthorityByUsername(username).forEach(role->
//            authorities.add(new SimpleGrantedAuthority((String) role))
//        );
        authorities.add(new SimpleGrantedAuthority(loginMapper.findAuthorityByUsername(username)));
        user.setAuthorities(authorities);
        log.info(user.getUsername()+"用户的所有权限为权限===>"+authorities.toString());
        return new User(user.getUsername(), passwordEncoder.encode(user.getPassword()),authorities);
    }

}

