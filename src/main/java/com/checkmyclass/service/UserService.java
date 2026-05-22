package com.checkmyclass.service;

import com.checkmyclass.domain.Role;
import com.checkmyclass.domain.User;
import com.checkmyclass.domain.Major;
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.repository.UserRepository;
import com.checkmyclass.repository.MajorRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // 🌟 1. 암호화 기계 임포트!
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final PasswordEncoder passwordEncoder; // 🌟 2. 암호화 기계 선언!

    // 🌟 3. 생성자에 암호화 기계를 넣어서 스프링이 챙겨오게 만들기!
    public UserService(UserRepository userRepository, MajorRepository majorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(UserRegisterDto dto) {
        // 1. 빈 유저 엔티티 객체 생성
        User user = new User();

        // 2. DTO에서 꺼내서 엔티티에 하나씩 쏙쏙 세팅
        user.setUserName(dto.getUserName());
        user.setStudentNumber(dto.getStudentNumber());

        // 🌟 4. [해결!] 생 비밀번호를 그냥 넣지 않고, 암호화해서 집어넣기!
        String encodedPassword = passwordEncoder.encode(dto.getUserPassword());
        user.setUserPassword(encodedPassword);

        user.setPhoneNumber(dto.getPhoneNumber());

        // 3. 필수값인 user_id는 우선 학번(studentNumber)과 똑같이 사용하도록 매핑
        user.setUserId(dto.getStudentNumber());

        // 4. 교직원 체크박스 여부에 따라 ENUM 역할 부여
        if ("PROFESSOR".equals(dto.getRole())) {
            user.setRole(Role.PROFESSOR);
        } else {
            user.setRole(Role.STUDENT);
        }

        // 5. DB에 있는 학과만 검색해서 연결! 없으면 가입 막기!
        Major major = majorRepository.findByMajorName(dto.getMajorName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. 정확한 학과명을 입력해주세요."));
        user.setMajor(major);

        // 6. 최종적으로 완벽해진 유저 객체를 DB에 쾅! 저장
        userRepository.save(user);
    }

    public User login(String studentNumber, String rawPassword) {
        // 1. 학번으로 DB에서 유저 찾기
        User user = userRepository.findByStudentNumber(studentNumber).orElse(null);

        // 2. 유저가 존재하고, 입력한 비번(raw)과 DB의 암호화된 비번이 일치하면 통과!
        if (user != null && passwordEncoder.matches(rawPassword, user.getUserPassword())) {
            return user; // 로그인 성공
        }

        return null; // 로그인 실패
    }
}