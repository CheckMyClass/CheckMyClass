package com.checkmyclass.repository;

import com.checkmyclass.domain.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 학과(Major) 데이터 접근 Repository
public interface MajorRepository extends JpaRepository<Major, Integer> {

    // 회원가입 시 학과명으로 학과 조회
    Optional<Major> findByMajorName(String majorName);
}
