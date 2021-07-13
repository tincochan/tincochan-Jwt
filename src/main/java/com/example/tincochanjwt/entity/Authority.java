package com.example.tincochanjwt.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Authority implements Serializable {
    private String roles;
}
