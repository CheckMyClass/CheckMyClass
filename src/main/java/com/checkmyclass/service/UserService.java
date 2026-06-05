package com.checkmyclass.service;

import com.checkmyclass.domain.Role;
import com.checkmyclass.domain.User;
import com.checkmyclass.domain.Major;
import com.checkmyclass.dto.UserRegisterDto;
import com.checkmyclass.repository.UserRepository;
import com.checkmyclass.repository.MajorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 회원 가입 / 로그인 / 정보 조회 처리 서비스
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화기

    public UserService(UserRepository userRepository, MajorRepository majorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.majorRepository = majorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입 처리
    @Transactional
    public void registerUser(UserRegisterDto dto) {
        User user = new User();

        // DTO 값을 엔티티에 세팅
        user.setUserName(dto.getUserName());
        user.setStudentNumber(dto.getStudentNumber());

        // 비밀번호는 암호화해서 저장
        String encodedPassword = passwordEncoder.encode(dto.getUserPassword());
        user.setUserPassword(encodedPassword);

        user.setPhoneNumber(dto.getPhoneNumber());

        // 로그인 아이디는 학번과 동일하게 설정
        user.setUserId(dto.getStudentNumber());

        // 교직원 체크 여부에 따라 권한 부여
        if ("PROFESSOR".equals(dto.getRole())) {
            user.setRole(Role.PROFESSOR);
        } else {
            user.setRole(Role.STUDENT);
        }

        // DB에 존재하는 학과만 연결, 없으면 가입 차단
        Major major = majorRepository.findByMajorName(dto.getMajorName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다. 정확한 학과명을 입력해주세요."));
        user.setMajor(major);

        userRepository.save(user);
    }

    // 로그인 처리 (성공 시 User 반환, 실패 시 null)
    public User login(String studentNumber, String rawPassword) {
        User user = userRepository.findByStudentNumber(studentNumber).orElse(null);

        // 입력한 비밀번호와 암호화된 비밀번호 비교
        if (user != null && passwordEncoder.matches(rawPassword, user.getUserPassword())) {
            return user;
        }

        return null;
    }

    // 학번으로 회원 정보 조회
    public User getUserInfo(String studentNumber) {
        return userRepository.findByStudentNumber(studentNumber).orElse(null);
    }
}
