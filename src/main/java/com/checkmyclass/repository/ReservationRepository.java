package com.checkmyclass.repository;

import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;

// 예약(Reservation) 데이터 접근 Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // 특정 회원의 예약 내역 조회 (최신순) - 마이페이지용
    List<Reservation> findByUser_IdOrderByIdDesc(Integer userId);

    // 특정 강의실의 특정 날짜 예약 조회 - 중복 예약 방지용
    List<Reservation> findByClassroomIdAndReservationDate(Integer classroomId, LocalDate date);

    // 승인 대기 목록 조회 (신청 순)
    List<Reservation> findByStatusOrderByCreateTimeAsc(ReservationStatus status);

    // 전체 예약 현황 조회 (최신순)
    @Query("SELECT r FROM Reservation r ORDER BY r.reservationDate DESC, r.startTime DESC")
    List<Reservation> findTop50ByOrderByDateDesc();

    // 강의실별 누적 예약 횟수 통계 (예약 많은 순, 동률 시 최근 예약 우선)
    @Query(value = "SELECT c.class_number, COUNT(r.id) " +
            "FROM reservation r " +
            "JOIN classroom c ON r.class_id = c.id " +
            "GROUP BY c.class_number " +
            "ORDER BY COUNT(r.id) DESC, AVG(UNIX_TIMESTAMP(r.reservation_date)) DESC",
            nativeQuery = true)
    List<Object[]> findClassroomReservationCounts();

    // 같은 강의실·날짜에 시간대가 겹치는 예약 존재 여부 (반려 건 제외)
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r " +
            "WHERE r.classroom.id = :classroomId AND r.reservationDate = :date " +
            "AND r.status != 'REJECTED' " +
            "AND (r.startTime < :endTime AND r.endTime > :startTime)")
    boolean existsOverlappingReservation(@Param("classroomId") Integer classroomId,
                                         @Param("date") LocalDate date,
                                         @Param("startTime") LocalTime startTime,
                                         @Param("endTime") LocalTime endTime);
}
