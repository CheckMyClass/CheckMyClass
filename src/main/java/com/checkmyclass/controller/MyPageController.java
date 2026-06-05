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

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService;

    // 🌟 1. 예약 현황 페이지 (기존 mypage 로직 -> 이름을 mypage-history로 변경)
    @GetMapping("/mypage/history")
    public String myPageHistory(HttpSession session, Model model) {
        String studentNumber = (String) session.getAttribute("userId");
        if (studentNumber == null) return "redirect:/";

        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        List<Reservation> historyList = myPageService.getMyReservationHistory(user.getId());
        model.addAttribute("historyList", historyList);

        return "mypage-history"; // templates/mypage-history.html 열기
    }

    // 🌟 2. 진짜 마이페이지 (새로 만드는 개인정보 확인용 페이지)
    @GetMapping("/mypage/profile")
    public String myPageProfile(HttpSession session, Model model) {
        String studentNumber = (String) session.getAttribute("userId");
        if (studentNumber == null) return "redirect:/";

        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        return "mypage-profile"; // templates/mypage-profile.html 열기
    }

    // 🌟 학생 본인이 예약 취소
    @PostMapping("/reservation/cancel")
    public String cancelReservation(@RequestParam Integer reservationId, RedirectAttributes redirectAttributes) {
        try {
            // 서비스의 취소(삭제) 메서드 호출
            myPageService.cancelReservation(reservationId);
            // 💡 주의: 만약 이 컨트롤러가 MyPageController가 아니고 ReservationController라면,
            // myPageService 부분을 위에서 주입받은 본인 서비스(reservationService 등)로 이름을 맞춰서 호출해야 해!

            redirectAttributes.addFlashAttribute("message", "예약이 성공적으로 취소되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "예약 취소 중 오류가 발생했습니다.");
        }

        return "redirect:/mypage/history"; // 취소 후 다시 예약 현황 페이지로 새로고침
    }

}