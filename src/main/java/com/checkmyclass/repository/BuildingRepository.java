package com.checkmyclass.repository;

import com.checkmyclass.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

// 건물(Building) 데이터 접근 Repository (기본 CRUD)
public interface BuildingRepository extends JpaRepository<Building, Integer> {
}
