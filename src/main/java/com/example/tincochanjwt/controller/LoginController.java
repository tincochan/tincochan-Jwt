package com.example.tincochanjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/toMain")
    public String main() {
        return "redirect:success.html";
    }

    @RequestMapping("/")
    public String demo() {
        return "main";
    }
    @RequestMapping("/logout")
    public String logout() {
        return "redirect:login.html";
    }
}
