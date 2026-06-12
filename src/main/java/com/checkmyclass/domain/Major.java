package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// 학과 정보 Entity (major 테이블과 매핑)
@Entity
@Table(name = "major")
@Getter
@NoArgsConstructor
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "major_name", nullable = false, length = 100)
    private String majorName;

    // 소속 단과대학 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id", nullable = false)
    private College college;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
}
