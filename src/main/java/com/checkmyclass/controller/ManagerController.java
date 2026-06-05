package com.checkmyclass.controller;

import com.checkmyclass.domain.Reservation; // 🌟 추가: Reservation 객체 위치 알려주기
import com.checkmyclass.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.checkmyclass.domain.ReservationStatus;

import java.util.List; // 🌟 추가: List 컬렉션 위치 알려주기

@Controller
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    // 🌟 1. 관리자 대시보드 (승인 대기 목록만 띄움)
    @GetMapping("/manager")
    public String managerPage(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("STAFF"))) {
            return "redirect:/";
        }

        // 대기 중(WAITING)인 예약만 가져오기
        List<Reservation> waitingList = managerService.getWaitingReservations();
        model.addAttribute("waitingList", waitingList);

        return "manager-request"; // manager-request.html
    }

    // 🌟 2. 관리자 예약 현황 (전체 내역 & 상태 변경 콤보박스)
    @GetMapping("/manager/history")
    public String managerHistoryPage(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("STAFF"))) {
            return "redirect:/";
        }

        // 전체 예약 내역 가져오기 (콤보박스에 쓸 용도)
        List<Reservation> allReservations = managerService.getAllReservations();
        model.addAttribute("allReservations", allReservations);

        return "manager-history"; // 새로 만들 manager-history.html
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
            // newStatus 문자를 ENUM으로 변환해서 서비스에 넘겨주기
            ReservationStatus status = ReservationStatus.valueOf(newStatus);

            // 💡 주의: managerService에 이 예약의 상태를 통째로 바꾸는 메서드를 만들어 둬야 해!
            // 예: managerService.updateReservationStatus(reservationId, status);
            managerService.changeStatus(reservationId, status);

            redirectAttributes.addFlashAttribute("message", "예약 상태가 변경되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "상태 변경 중 오류가 발생했습니다.");
        }

        return "redirect:/manager/history"; // 다시 전체 현황 페이지로 돌아가기
    }
}