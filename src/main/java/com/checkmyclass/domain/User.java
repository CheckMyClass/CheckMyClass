package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 회원 정보 Entity (users 테이블과 매핑)
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 로그인 아이디 (학번과 동일하게 사용)
    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    // 암호화되어 저장되는 비밀번호
    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    // 권한 등급 (문자열로 저장)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    // 소속 학과 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    // 저장 직전 가입 시각 자동 기록
    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
