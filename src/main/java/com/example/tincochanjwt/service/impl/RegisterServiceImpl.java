package com.example.tincochanjwt.service.impl;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.entity.UserDto;
import com.example.tincochanjwt.exception.RegisterFailureException;
import com.example.tincochanjwt.exception.RegisterUsernameHasBeenExists;
import com.example.tincochanjwt.mapper.RegisterMapper;
import com.example.tincochanjwt.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;
    @Override
    public boolean register(UserDto userDto) throws RegisterFailureException {
        boolean b = registerMapper.register(userDto);
        System.out.println(b);
        if (!b) {
            log.info("注册失败");
            throw new RegisterFailureException("注册失败");
        }
        return b;
    }

    @Override
    public User selectDup(String username) throws  RegisterUsernameHasBeenExists {
        User user = registerMapper.selectDup(username);
        if (user != null) {
            throw new RegisterUsernameHasBeenExists("用户名已经存在");
        }
        return null;
    }
}
