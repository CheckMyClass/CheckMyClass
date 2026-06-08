package com.checkmyclass.repository;

import com.checkmyclass.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 회원(User) 데이터 접근 Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 로그인 아이디로 회원 조회
    Optional<User> findByUserId(String userId);

    // 학번으로 회원 조회
    Optional<User> findByStudentNumber(String studentNumber);
}
