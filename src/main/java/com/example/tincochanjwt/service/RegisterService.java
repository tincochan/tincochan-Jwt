package com.example.tincochanjwt.service;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.entity.UserDto;
import com.example.tincochanjwt.exception.RegisterFailureException;
import com.example.tincochanjwt.exception.RegisterUsernameHasBeenExists;

public interface RegisterService {
    boolean register(UserDto userDto) throws RegisterFailureException;
    User selectDup(String username) throws RegisterUsernameHasBeenExists;
}

