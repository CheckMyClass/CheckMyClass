package com.checkmyclass.service;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// 마이페이지 서비스 (예약 내역 조회 / 취소)
@Service
@Transactional(readOnly = true) // 조회 위주이므로 읽기 전용 기본 설정
public class MyPageService {

    private final ReservationRepository reservationRepository;

    public MyPageService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 특정 회원의 예약 내역 조회
    public List<Reservation> getMyReservationHistory(Integer userId) {
        return reservationRepository.findByUser_IdOrderByIdDesc(userId);
    }

    // 예약 취소 (삭제) - 쓰기 작업이므로 읽기 전용 해제
    @Transactional
    public void cancelReservation(Integer reservationId, Integer userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot cancel another user's reservation.");
        }

        reservationRepository.delete(reservation);
    }
}
