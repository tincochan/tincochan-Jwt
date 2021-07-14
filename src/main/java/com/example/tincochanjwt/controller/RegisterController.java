package com.example.tincochanjwt.controller;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.entity.UserDto;
import com.example.tincochanjwt.exception.RegisterFailureException;
import com.example.tincochanjwt.exception.RegisterUsernameHasBeenExists;
import com.example.tincochanjwt.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @RequestMapping("/register")
    public String register(@Validated UserDto userDto) throws RegisterUsernameHasBeenExists {
        log.info("注册的数据为:"+userDto.toString() );

        User user = registerService.selectDup(userDto.getUsername());
        if (user == null) {
            throw new RegisterUsernameHasBeenExists("用户已存在");
        }
        try {
            registerService.register(userDto);
        } catch (RegisterFailureException e) {
            e.printStackTrace();
        }
        return "/";
    }
}

