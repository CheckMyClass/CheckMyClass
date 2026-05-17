package com.checkmyclass.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "class") // DB 테이블 이름은 class
@Getter
@NoArgsConstructor
public class Classroom { // 자바 클래스 이름은 Classroom으로 회피!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_number", nullable = false, length = 20)
    private String classNumber;

    // ★ 핵심: Building(건물) 테이블과 외래키 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "capacity_number")
    private Integer capacityNumber;

    @Column(name = "is_practical")
    private Boolean isPractical; // tinyint(1)은 Boolean으로 찰떡 매핑됨!

    @Column(name = "board_type", length = 50)
    private String boardType;

    @Column(name = "image_URL", columnDefinition = "TEXT")
    private String imageUrl;
}