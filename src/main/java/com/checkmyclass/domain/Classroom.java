package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 강의실 정보 Entity (classroom 테이블과 매핑)
@Entity
@Table(name = "classroom")
@Getter
@NoArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_number", nullable = false, length = 20)
    private String classNumber;

    // 소속 건물 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    // 소속 학과 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "capacity_number")
    private Integer capacityNumber;

    // 실습실(PC) 여부
    @Column(name = "is_practical")
    private Boolean isPractical;

    // 칠판 종류 (전자칠판 / 빔프로젝터)
    @Column(name = "board_type", length = 50)
    private String boardType;

    @Column(name = "image_URL", columnDefinition = "TEXT")
    private String imageUrl;
}
