package com.example.tincochanjwt.mapper;

import com.example.tincochanjwt.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginMapper {
    User findByUsername(String username);
    String findAuthorityByUsername(String username);
}
