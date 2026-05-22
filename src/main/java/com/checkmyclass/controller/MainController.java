package com.checkmyclass.controller;

import com.checkmyclass.domain.User; // 🌟 이거 하나 빠져있었어!
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    private final UserService userService;

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

    // 3. 회원가입 데이터 처리 및 유효성 검사 (POST)
    @PostMapping("/register")
    public String registerProcess(@Valid UserRegisterDto dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "register";
        }

        try {
            userService.registerUser(dto);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다! 로그인해주세요.");
            return "redirect:/";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입 중 서버 오류가 발생했습니다.");
            return "register";
        }
    }

    // 4. 로그인 데이터 처리 (POST)
    @PostMapping("/login")
    public String loginProcess(@RequestParam String studentNumber,
                               @RequestParam String userPassword,
                               HttpSession session,
                               Model model) {

        User loginUser = userService.login(studentNumber, userPassword);

        if (loginUser == null) {
            model.addAttribute("errorMessage", "학번 또는 비밀번호가 일치하지 않습니다.");
            return "login";
        }

        session.setAttribute("userId", loginUser.getUserId());
        session.setAttribute("role", loginUser.getRole().name());

        String userRole = loginUser.getRole().name();
        if ("ADMIN".equals(userRole) || "STAFF".equals(userRole)) {
            return "redirect:/manager";
        }

        // 🌟 나중에 학생/교수용 메인 화면(예: /dashboard)을 만들면 꼭 여기를 바꿔줘!
        return "redirect:/";
    }
}