package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users") // 실제 DB 테이블 이름 매핑
@Getter
@NoArgsConstructor
public class User {

    @Id // 기본키(PK) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING) // ENUM 타입을 문자열 그대로 저장 ('STUDENT', 'PROFESSOR' 등)
    private Role role; // 별도의 Role enum 클래스를 만들어야 함

    // ★ 핵심: 외래키(FK) 매핑 (Major 테이블과 다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
}