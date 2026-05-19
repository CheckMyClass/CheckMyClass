package com.checkmyclass.controller;

import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    // 🌟 2. 회원가입 화면 띄우기 (GET) - 이게 지워져서 405 에러가 났던 거야!
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // 3. 회원가입 데이터 처리 및 유효성 검사 (POST)
    @PostMapping("/register")
    public String registerProcess(@Valid UserRegisterDto dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // 1. DTO 조건 검사에서 걸린 에러가 있는지 확인!
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "register";
        }

        try {
            // 2. 조건 검사를 무사히 통과하면 DB 저장 시도
            userService.registerUser(dto);

            // 성공 팝업과 함께 로그인 화면으로 점프
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
}