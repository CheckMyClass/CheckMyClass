package com.checkmyclass.controller;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.User;
import com.checkmyclass.service.MyPageService;
import com.checkmyclass.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// 마이페이지 담당 컨트롤러 (예약 현황 / 개인정보 / 예약 취소)
@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService;

    // 예약 현황 페이지
    @GetMapping("/mypage/history")
    public String myPageHistory(HttpSession session, Model model) {
        String studentNumber = (String) session.getAttribute("userId");
        if (studentNumber == null) return "redirect:/";

        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        List<Reservation> historyList = myPageService.getMyReservationHistory(user.getId());
        model.addAttribute("historyList", historyList);

        return "mypage-history";
    }

    // 개인정보 확인 페이지
    @GetMapping("/mypage/profile")
    public String myPageProfile(HttpSession session, Model model) {
        String studentNumber = (String) session.getAttribute("userId");
        if (studentNumber == null) return "redirect:/";

        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        return "mypage-profile";
    }

    // 학생 본인 예약 취소
    @PostMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam Integer reservationId, RedirectAttributes redirectAttributes) {
        try {
            myPageService.cancelReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "예약 취소 중 오류가 발생했습니다.");
        }

        return "redirect:/mypage/history";
    }
}
