package com.checkmyclass.repository;

import com.checkmyclass.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    // 기본 CRUD만으로 충분함
}