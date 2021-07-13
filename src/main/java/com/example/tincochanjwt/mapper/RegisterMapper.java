package com.example.tincochanjwt.mapper;

import com.example.tincochanjwt.entity.User;
import com.example.tincochanjwt.entity.UserDto;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterMapper {
    boolean register(UserDto userDto);
    User selectDup(String username);
}
