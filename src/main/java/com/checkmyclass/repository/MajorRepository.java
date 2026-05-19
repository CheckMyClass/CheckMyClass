package com.checkmyclass.repository;

import com.checkmyclass.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Integer> {
    // 학과 이름으로 기존 학과가 있는지 찾아보는 심부름꾼 기능 추가
    Optional<Major> findByMajorName(String majorName);
    // ※ 만약 Major 엔티티 안의 변수명이 majorName이 아니라 name 같은 거라면 findByName으로 바꿔줘야 해!
}