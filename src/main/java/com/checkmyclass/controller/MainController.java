package com.checkmyclass.controller;

import com.checkmyclass.domain.User;
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.service.UserService;
import com.checkmyclass.repository.ReservationRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

// 로그인 / 회원가입 / 메인 페이지 담당 컨트롤러
@Controller
public class MainController {

    private final UserService userService;
    private final ReservationRepository reservationRepository;

    public MainController(UserService userService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
    }

    // 로그인 화면
    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 화면
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    // 회원가입 처리 (유효성 검사 포함)
    @PostMapping("/register")
    public String registerProcess(@Valid UserRegisterDto dto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        // 입력값 검증 실패 시 첫 번째 에러 메시지 표시
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

    // 로그인 처리
    @PostMapping("/login")
    public String loginProcess(@RequestParam String studentNumber,
                               @RequestParam String userPassword,
                               HttpSession session,
                               Model model) {

        User loginUser = userService.login(studentNumber, userPassword);

        // 로그인 실패
        if (loginUser == null) {
            model.addAttribute("errorMessage", "학번 또는 비밀번호가 일치하지 않습니다.");
            return "login";
        }

        // 세션에 로그인 정보 저장
        session.setAttribute("userId", loginUser.getUserId());
        session.setAttribute("role", loginUser.getRole().name());

        // 관리자/교직원은 관리자 페이지로, 그 외는 메인으로
        String userRole = loginUser.getRole().name();
        if ("ADMIN".equals(userRole) || "PROFESSOR".equals(userRole)) {
            return "redirect:/manager";
        }

        return "redirect:/main";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 메인 페이지 (회원 정보 + 인기 강의실 TOP 3)
    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        String studentNumber = (String) session.getAttribute("userId");

        // 미로그인 시 로그인 화면으로
        if (studentNumber == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "login";
        }

        // 회원 정보 조회
        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        // 인기 강의실 통계에서 상위 3개만 추출
        List<Object[]> allStats = reservationRepository.findClassroomReservationCounts();
        List<Object[]> top3Stats = allStats.size() > 3 ? allStats.subList(0, 3) : allStats;
        model.addAttribute("topStats", top3Stats);

        return "main";
    }
}
