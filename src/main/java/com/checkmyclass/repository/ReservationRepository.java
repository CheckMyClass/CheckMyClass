package com.checkmyclass.repository;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    // 1. 특정 유저(user_id)가 예약한 내역 싹 다 가져오기 (마이페이지용)
    List<Reservation> findByUserId(Integer userId);

    // 2. 특정 강의실의 특정 날짜에 잡힌 예약들 가져오기 (중복 예약 방지용)
    List<Reservation> findByClassroomIdAndReservationDate(Integer classroomId, LocalDate date);

    // 3. 관리자용 - 승인 대기 목록 (신청 순서대로)
    List<Reservation> findByStatusOrderByCreateTimeAsc(ReservationStatus status);

    // 4. 관리자용 - 전체 예약 현황 최신순
    @Query("SELECT r FROM Reservation r ORDER BY r.reservationDate DESC, r.startTime DESC")
    List<Reservation> findTop50ByOrderByDateDesc();

    // 🌟 5. [수정됨!] 관리자용 - 강의실별 예약 횟수 통계
    // class -> classroom 으로 변경!
    // r.class_id -> r.classroom_id 로 변경!
    @Query(value = "SELECT c.class_number, COUNT(r.id) " +
            "FROM reservation r " +
            "JOIN classroom c ON r.class_id = c.id " +
            "GROUP BY c.class_number " +
            "ORDER BY COUNT(r.id) DESC, AVG(UNIX_TIMESTAMP(r.reservation_date)) DESC",
            nativeQuery = true)
    List<Object[]> findClassroomReservationCounts();
}