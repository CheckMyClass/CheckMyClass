package com.checkmyclass.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "login"; // templates/login.html 파일을 브라우저에 보여줘라!
    }

    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html 파일을 브라우저에 보여줘라!
    }
}