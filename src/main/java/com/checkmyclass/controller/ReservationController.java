package com.checkmyclass.controller;

import com.checkmyclass.domain.Classroom;
import com.checkmyclass.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 예약 신청 페이지 / 예약 처리 담당 컨트롤러
@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 신청 화면 (날짜·시간·장비 필터로 강의실 검색)
    @GetMapping("/reservation")
    public String reservationPage(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime start_time,
            @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime end_time,
            @RequestParam(required = false, name = "filter") List<String> filters,
            HttpSession session, Model model) {

        // 로그인 체크
        if (session.getAttribute("userId") == null) {
            return "redirect:/";
        }

        // 값이 없으면 기본값 설정 (오늘, 17:00~18:00)
        if (date == null) date = LocalDate.now();
        if (start_time == null) start_time = LocalTime.of(17, 0);
        if (end_time == null) end_time = LocalTime.of(18, 0);
        if (filters == null) filters = new ArrayList<>();

        String dateDisplay = date.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));

        // 필터 조건에 맞는 강의실 조회
        List<Classroom> classrooms = reservationService.searchClassrooms(filters);

        // 화면에 데이터 전달
        model.addAttribute("date", date);
        model.addAttribute("start_time", start_time);
        model.addAttribute("end_time", end_time);
        model.addAttribute("filters", filters);
        model.addAttribute("classrooms", classrooms);
        model.addAttribute("dateDisplay", dateDisplay);

        return "reservation";
    }

    // 예약 폼 제출 처리
    @PostMapping("/reservation/process")
    public String processReservation(
            @RequestParam Integer class_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime start_time,
            @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime end_time,
            @RequestParam String purpose,
            HttpSession session, RedirectAttributes redirectAttributes) {

        String studentNumber = (String) session.getAttribute("userId");
        if (studentNumber == null) return "redirect:/";

        try {
            // 예약 저장
            reservationService.makeReservation(studentNumber, class_id, date, start_time, end_time, purpose);
            redirectAttributes.addFlashAttribute("message", "예약 신청이 완료되었습니다.");
            return "redirect:/mypage/history";

        } catch (IllegalArgumentException e) {
            // 실패 시 에러 메시지와 함께 예약 화면으로 복귀
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/reservation?date=" + date + "&start_time=" + start_time + "&end_time=" + end_time;
        }
    }
}
