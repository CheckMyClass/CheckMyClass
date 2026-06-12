package com.checkmyclass.controller;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import com.checkmyclass.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// 관리자 페이지 담당 컨트롤러 (승인 대기 / 전체 현황 / 상태 변경)
@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    // 관리자 대시보드 (승인 대기 목록)
    @GetMapping("/manager")
    public String managerPage(HttpSession session, Model model) {
        // 관리자/교직원 권한 체크
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("PROFESSOR"))) {
            return "redirect:/";
        }

        List<Reservation> waitingList = managerService.getWaitingReservations();
        model.addAttribute("waitingList", waitingList);

        return "manager-request";
    }

    // 전체 예약 현황 (상태 변경 콤보박스 포함)
    @GetMapping("/manager/history")
    public String managerHistoryPage(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("PROFESSOR"))) {
            return "redirect:/";
        }

        List<Reservation> allReservations = managerService.getAllReservations();
        model.addAttribute("allReservations", allReservations);

        return "manager-history";
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

    // 예약 상태 변경 (콤보박스 선택값으로 변경)
    @PostMapping("/manager/update-status")
    public String updateStatus(@RequestParam Integer reservationId,
                               @RequestParam String newStatus,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            return "redirect:/";
        }

        try {
            // 문자열을 ENUM으로 변환 후 상태 변경
            ReservationStatus status = ReservationStatus.valueOf(newStatus);
            managerService.changeStatus(reservationId, status);

            redirectAttributes.addFlashAttribute("message", "예약 상태가 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "상태 변경 중 오류가 발생했습니다.");
        }

        return "redirect:/manager/history";
    }
}
