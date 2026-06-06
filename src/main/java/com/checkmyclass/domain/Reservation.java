package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

// 강의실 예약 정보 Entity (reservation 테이블과 매핑)
@Entity
@Table(name = "reservation")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 예약한 회원 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 예약된 강의실 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private Classroom classroom;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(nullable = false, length = 255)
    private String purpose;

    // 예약 상태 (WAITING / APPROVED / REJECTED)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReservationStatus status;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    // 예약 상태 변경
    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
