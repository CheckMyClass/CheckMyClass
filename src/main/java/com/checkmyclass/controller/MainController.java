package com.checkmyclass.controller;

import com.checkmyclass.domain.User;
import com.checkmyclass.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private final UserService userService;

    // UserService 불러오기
    public MainController(UserService userService) {
        this.userService = userService;
    }

    // 1. 로그인 화면 띄우기 (GET)
    @GetMapping("/")
    public String home() {
        return "login";
    }

    // 2. 회원가입 화면 띄우기 (GET)
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // 3. 🌟 회원가입 데이터 받아서 처리하기 (POST)
    @PostMapping("/register")
    public String registerProcess(User user) {
        // 화면에서 넘어온 user 데이터를 서비스로 넘겨서 DB에 저장!
        userService.registerUser(user);
        
        return "redirect:/";
    }
}