package com.checkmyclass.repository;

import com.checkmyclass.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    // 1. 특정 유저(user_id)가 예약한 내역 싹 다 가져오기 (마이페이지용)
    List<Reservation> findByUserId(Integer userId);

    // 2. 특정 강의실의 특정 날짜에 잡힌 예약들 가져오기 (중복 예약 방지용)
    List<Reservation> findByClassroomIdAndReservationDate(Integer classroomId, LocalDate date);
}