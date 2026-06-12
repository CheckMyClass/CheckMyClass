package com.checkmyclass.repository;

import com.checkmyclass.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// 강의실(Classroom) 데이터 접근 Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    // 특정 건물에 속한 강의실 목록 조회
    List<Classroom> findByBuildingId(Integer buildingId);
}
