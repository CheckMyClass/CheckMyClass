package com.checkmyclass.repository;

import com.checkmyclass.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    // 기본 CRUD만으로 충분함
    // 회원가입 시 학과명으로 Major 찾기
    Optional<Major> findByMajorName(String majorName);
}