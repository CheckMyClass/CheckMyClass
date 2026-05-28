package com.checkmyclass.controller;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.service.MyPageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class MyPageController {

    private final MyPageService myPageService;

    public MyPageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        // 1. 현재 로그인한 유저 확인
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            return "redirect:/login"; // 로그인 안 되어있으면 튕겨내기
        }
        // 임의적 로그인
//        Integer userId = 1;

        // 2. 서비스에게 내 예약 내역 가져오라고 시키기
        List<Reservation> historyList = myPageService.getMyReservationHistory(userId);

        // 3. HTML 화면에 데이터를 넘겨주기 위해 Model에 담기 ("historyList"라는 이름표를 붙임)
        model.addAttribute("historyList", historyList);

        return "mypage"; // resources/templates/mypage.html 파일 열기
    }
}