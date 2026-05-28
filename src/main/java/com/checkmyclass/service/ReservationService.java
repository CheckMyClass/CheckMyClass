package com.checkmyclass.service;

import com.checkmyclass.domain.Classroom;
import com.checkmyclass.domain.Reservation;
import com.checkmyclass.domain.ReservationStatus;
import com.checkmyclass.domain.User;
import com.checkmyclass.repository.ClassroomRepository;
import com.checkmyclass.repository.ReservationRepository;
import com.checkmyclass.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    // 1. 강의실 필터링 검색
    public List<Classroom> searchClassrooms(List<String> filters) {
        List<Classroom> allRooms = classroomRepository.findAll();

        if (filters == null || filters.isEmpty()) return allRooms;

        // 필터 조건에 맞는 강의실만 걸러내기
        return allRooms.stream().filter(room -> {
            if (filters.contains("pc") && (room.getIsPractical() == null || !room.getIsPractical())) return false;
            if (filters.contains("projector") && !"빔프로젝터".equals(room.getBoardType())) return false;
            if (filters.contains("board") && !"전자칠판".equals(room.getBoardType())) return false;
            return true;
        }).collect(Collectors.toList());
    }

    // 2. 예약 확정 및 저장
    @Transactional
    public void makeReservation(String studentNumber, Integer classId, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose) {
        // 시간 유효성 검사
        if (startTime.compareTo(endTime) >= 0) {
            throw new IllegalArgumentException("종료 시간은 시작 시간보다 늦어야 합니다.");
        }

        // 중복 예약 체크
        if (reservationRepository.existsOverlappingReservation(classId, date, startTime, endTime)) {
            throw new IllegalArgumentException("해당 시간에는 이미 다른 예약이 존재합니다.");
        }

        User user = userRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new IllegalArgumentException("유저 정보를 찾을 수 없습니다."));
        Classroom classroom = classroomRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("강의실 정보를 찾을 수 없습니다."));

        // 예약 생성
        Reservation res = new Reservation();
        // ★ 주의: Reservation 엔티티에 setUser, setClassroom 등의 setter 메서드가 있어야 해!
        // 만약 없다면 빌더 패턴이나 생성자를 사용해야 해.
        res.setUser(user);
        res.setClassroom(classroom);
        res.setReservationDate(date);
        res.setStartTime(startTime);
        res.setEndTime(endTime);
        res.setPurpose(purpose);
        res.updateStatus(ReservationStatus.WAITING);
        // res.setCreateTime(LocalDateTime.now()); (엔티티 설정에 따라 필요시 추가)

        reservationRepository.save(res);
    }
}