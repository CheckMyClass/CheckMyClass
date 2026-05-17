package com.checkmyclass.repository;

import com.checkmyclass.domain.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
    // 기본 CRUD만으로 충분함
}