package com.checkmyclass.service;

import com.checkmyclass.domain.User;
import com.checkmyclass.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service // 이 클래스는 비즈니스 로직을 담당하는 서비스라고 선언!
public class UserService {

    private final UserRepository userRepository;

    // DB 심부름꾼(UserRepository)을 서비스로 불러오기
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🌟 회원가입 기능 (데이터베이스에 유저 정보 저장)
    public void registerUser(User user) {
        // 나중에는 여기서 비밀번호 암호화도 할 거야. 지금은 일단 그대로 저장!
        userRepository.save(user);
    }
}