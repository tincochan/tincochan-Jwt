package com.example.tincochanjwt.controller;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@EnableWebSecurity
public class TestController {

    @RequestMapping("/add")
    public String add() {
        return "add";
    }

    @RequestMapping("select")
    public String select() {
        return "select";
    }

    @RequestMapping("/delete")
    public String delete() {
        return "delete";
    }

    @RequestMapping("/update")
    public String update() {
        return "update";
    }
}
