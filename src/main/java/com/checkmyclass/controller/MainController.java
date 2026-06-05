package com.checkmyclass.controller;

import com.checkmyclass.domain.User;
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.service.UserService;
import com.checkmyclass.repository.ReservationRepository; // 🌟 추가: 예약 정보 가져올 심부름꾼
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List; // 🌟 추가: 랭킹 리스트를 담기 위해 필요

@Controller
public class MainController {

    private final UserService userService;
    private final ReservationRepository reservationRepository; // 🌟 추가

    // 🌟 수정: 생성자에 reservationRepository 추가해서 의존성 주입받기
    public MainController(UserService userService, ReservationRepository reservationRepository) {
        this.userService = userService;
        this.reservationRepository = reservationRepository;
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

        return "redirect:/main";
    }

    // 🌟 5. 일반 회원(학생/교수) 메인 페이지 띄우기
    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model) {
        // 1. 세션에서 로그인한 유저 아이디(학번) 꺼내기
        String studentNumber = (String) session.getAttribute("userId");

        // 2. 로그인이 안 되어있다면 얄짤없이 로그인 화면으로 쫓아내기
        if (studentNumber == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "login";
        }

        // 3. DB에서 유저의 모든 정보(이름, 학과, 역할 등) 가져오기
        User user = userService.getUserInfo(studentNumber);

        // 4. 화면(HTML)에 유저 정보 넘겨주기
        model.addAttribute("user", user);

        // 🌟 5. 메인 화면용 인기 강의실 통계 가져오기 (추가된 핵심 코드!)
        List<Object[]> allStats = reservationRepository.findClassroomReservationCounts();

        // 데이터가 3개보다 많으면 TOP 3만 자르고, 적으면 있는 대로 가져오기
        List<Object[]> top3Stats = allStats.size() > 3 ? allStats.subList(0, 3) : allStats;
        model.addAttribute("topStats", top3Stats);

        return "main"; // main.html 띄우기
    }
}