package com.checkmyclass.repository;

import com.checkmyclass.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // 아이디로 유저 찾기 (로그인용)
    Optional<User> findByUserId(String userId);

    // 학번으로 유저 찾기
    Optional<User> findByStudentNumber(String studentNumber);
}