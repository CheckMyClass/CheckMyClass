package com.checkmyclass.controller;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.User; // 🌟 추가
import com.checkmyclass.service.MyPageService;
import com.checkmyclass.service.UserService; // 🌟 유저 정보 가져오기 위해 추가
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor // 🌟 팀원 코드의 생성자 덩어리를 이 한 줄로 깔끔하게 압축!
public class MyPageController {

    private final MyPageService myPageService;
    private final UserService userService; // 🌟 내 프로필 정보 가져올 서비스 추가

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        // 1. 현재 로그인한 유저 확인 (세션 키 이름은 로그인 세팅과 꼭 맞춰야 해!)
        String studentNumber = (String) session.getAttribute("userId"); // 🌟 아까 메인에서 쓰던 방식으로 통일

        if (studentNumber == null) {
            return "redirect:/"; // 로그인 안 되어있으면 튕겨내기
        }

        // 2. 내 프로필 정보 가져와서 화면에 넘기기 (이게 없으면 화면 터짐!)
        User user = userService.getUserInfo(studentNumber);
        model.addAttribute("user", user);

        // 3. 내 예약 내역 가져오기 (팀원이 짠 로직 그대로 활용되게 PK인 id를 넘김)
        List<Reservation> historyList = myPageService.getMyReservationHistory(user.getId());
        model.addAttribute("historyList", historyList);

        return "mypage";
    }
}