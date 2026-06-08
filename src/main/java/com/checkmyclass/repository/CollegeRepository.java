package com.checkmyclass.repository;

import com.checkmyclass.domain.College;
import org.springframework.data.jpa.repository.JpaRepository;

// 단과대학(College) 데이터 접근 Repository (기본 CRUD)
public interface CollegeRepository extends JpaRepository<College, Integer> {
}
