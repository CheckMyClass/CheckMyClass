package com.checkmyclass.service;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true) // 데이터 조회만 하니까 성능을 위해 읽기 전용 모드 켜기!
public class MyPageService {

    private final ReservationRepository reservationRepository;

    public MyPageService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    // 특정 유저의 예약 내역 목록을 가져오는 기능
    public List<Reservation> getMyReservationHistory(Integer userId) {
        return reservationRepository.findByUser_IdOrderByCreateTimeAsc(userId);
    }

    @org.springframework.transaction.annotation.Transactional
    public void cancelReservation(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}