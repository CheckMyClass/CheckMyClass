package com.checkmyclass.repository;

import com.checkmyclass.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    // 특정 건물(buildingId)에 있는 강의실 목록 다 가져오기
    List<Classroom> findByBuildingId(Integer buildingId);
}