package com.checkmyclass.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 건물 정보 Entity (building 테이블과 매핑)
@Entity
@Table(name = "building")
@Getter
@NoArgsConstructor
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "building_name", nullable = false, length = 100)
    private String buildingName;

    @Column(name = "building_number", length = 20)
    private String buildingNumber;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

    // 이 건물에 속한 강의실 목록 (일대다 관계)
    @OneToMany(mappedBy = "building")
    private List<Classroom> classrooms = new ArrayList<>();
}
