package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter; // 🌟 추가
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter // 🌟 스프링이 데이터를 주입할 수 있도록 세터 오픈!
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "student_number", nullable = false, length = 20)
    private String studentNumber;

    @Column(name = "phone_number", nullable = false, length = 20) // 🌟 누락됐던 전화번호 컬럼 추가!
    private String phoneNumber;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}