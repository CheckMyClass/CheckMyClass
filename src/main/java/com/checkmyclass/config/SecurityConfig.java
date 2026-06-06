package com.checkmyclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 시큐리티 설정 (비밀번호 암호화 / 접근 권한)
@Configuration
public class SecurityConfig {

    // 비밀번호 암호화에 사용할 BCrypt 인코더 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 기본 로그인 폼 비활성화 + 직접 만든 화면 사용을 위한 접근 허용 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 폼 전송 테스트용으로 CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청 허용 (추후 권한별 제한 가능)
                );
        return http.build();
    }
}
