package com.checkmyclass.service;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import com.checkmyclass.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ReservationRepository reservationRepository;

    // 승인 대기 목록 조회
    public List<Reservation> getWaitingReservations() {
        return reservationRepository.findByStatusOrderByCreateTimeAsc(ReservationStatus.WAITING);
    }

    // 전체 예약 현황 (최신순)
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

    // 🌟 콤보박스에서 넘겨받은 상태로 예약을 마음대로 변경하는 메서드
    @Transactional
    public void changeStatus(Integer reservationId, ReservationStatus newStatus) {
        // 1. DB에서 해당 예약 번호(ID)를 가진 예약을 찾아온다.
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        // 2. 예약의 상태를 콤보박스에서 선택한 새로운 상태(newStatus)로 바꾼다.
        reservation.updateStatus(newStatus);

        // (💡 @Transactional이 붙어있어서 save를 따로 안 해도 변경이 감지되어 DB에 자동 저장돼!)
    }
}
