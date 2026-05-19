package com.checkmyclass.service;

import com.checkmyclass.domain.Role;
import com.checkmyclass.domain.User;
import com.checkmyclass.domain.Major;
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.repository.UserRepository;
import com.checkmyclass.repository.MajorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MajorRepository majorRepository;

    public UserService(UserRepository userRepository, MajorRepository majorRepository) {
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
    }

    @Transactional
    public void registerUser(UserRegisterDto dto) {
        // 1. 빈 유저 엔티티 객체 생성
        User user = new User();

        // 2. DTO에서 꺼내서 엔티티에 하나씩 쏙쏙 세팅
        user.setUserName(dto.getUserName());
        user.setStudentNumber(dto.getStudentNumber());
        user.setUserPassword(dto.getUserPassword()); // 🔐 나중엔 여기에 암호화 로직이 들어갈 거야!
        user.setPhoneNumber(dto.getPhoneNumber());

        // 3. [해결!] 필수값인 user_id는 우선 학번(studentNumber)과 똑같이 사용하도록 매핑
        user.setUserId(dto.getStudentNumber());

        // 4. [해결!] 교직원 체크박스 여부에 따라 ENUM 역할 부여
        if ("PROFESSOR".equals(dto.getRole())) {
            user.setRole(Role.PROFESSOR);
        } else {
            user.setRole(Role.STUDENT);
        }

        // 5. 🌟 [수정!] DB에 있는 학과만 검색해서 연결! 없으면 가입 막기!
        Major major = majorRepository.findByMajorName(dto.getMajorName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. 정확한 학과명을 입력해주세요."));
        user.setMajor(major);

        // 6. 최종적으로 완벽해진 유저 객체를 DB에 쾅! 저장
        userRepository.save(user);
    }
}