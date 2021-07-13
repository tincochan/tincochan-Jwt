package com.example.tincochanjwt.utils;

import lombok.Data;
import java.io.Serializable;

@Data
public  class ResponseBody<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    public ResponseBody(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ResponseBody(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
