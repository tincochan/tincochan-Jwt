package com.example.tincochanjwt.entity;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
public class UserDto implements Serializable {

    @NotNull
    @Size(min = 3, max = 15, message = "用户名必须在3~15之间")
    private String username;

    @NotNull
    @Size(min = 5, max = 15, message = "密码必须在5~15之间")
    private String password;
}

