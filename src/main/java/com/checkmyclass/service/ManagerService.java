package com.checkmyclass.service;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import com.checkmyclass.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 관리자 예약 관리 서비스 (승인 / 반려 / 통계)
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ReservationRepository reservationRepository;

    // 승인 대기 목록 조회
    public List<Reservation> getWaitingReservations() {
        return reservationRepository.findByStatusOrderByCreateTimeAsc(ReservationStatus.WAITING);
    }

    // 전체 예약 현황 조회 (최신순)
    public List<Reservation> getAllReservations() {
        return reservationRepository.findTop50ByOrderByDateDesc();
    }

    // 강의실별 예약 횟수 통계
    public List<Object[]> getClassroomStats() {
        return reservationRepository.findClassroomReservationCounts();
    }

    // 예약 승인
    @Transactional
    public void approveReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.updateStatus(ReservationStatus.APPROVED);
    }

    // 예약 반려
    @Transactional
    public void rejectReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.updateStatus(ReservationStatus.REJECTED);
    }

    // 콤보박스에서 선택한 상태로 예약 상태 변경
    // (@Transactional 변경 감지로 save 없이 자동 반영)
    @Transactional
    public void changeStatus(Integer reservationId, ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        reservation.updateStatus(newStatus);
    }
}
