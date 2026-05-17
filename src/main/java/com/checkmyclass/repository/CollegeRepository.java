package com.checkmyclass.repository;

import com.checkmyclass.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Integer> {
    // 기본 CRUD만으로 충분함
}