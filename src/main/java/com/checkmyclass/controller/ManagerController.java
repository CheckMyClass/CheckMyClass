package com.checkmyclass.controller;

import com.checkmyclass.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    // 관리자 페이지
    @GetMapping("/manager")
    public String managerPage(HttpSession session, Model model) {

        // 로그인 및 권한 체크 (테스트 시 아래 4줄 주석 처리 가능)
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("STAFF"))) {
            return "redirect:/";
        }

        model.addAttribute("waitingList", managerService.getWaitingReservations());
        model.addAttribute("allReservations", managerService.getAllReservations());
        model.addAttribute("stats", managerService.getClassroomStats());

        return "manager";
    }

    // 예약 승인
    @PostMapping("/manager/approve")
    public String approve(@RequestParam Integer reservationId,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {

        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/";
        }

        try {
            managerService.approveReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "예약이 승인되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "처리 중 오류가 발생했습니다.");
        }

        return "redirect:/manager";
    }

    // 예약 반려
    @PostMapping("/manager/reject")
    public String reject(@RequestParam Integer reservationId,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/";
        }

        try {
            managerService.rejectReservation(reservationId);
            redirectAttributes.addFlashAttribute("message", "예약이 반려되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "처리 중 오류가 발생했습니다.");
        }

        return "redirect:/manager";
    }
}
