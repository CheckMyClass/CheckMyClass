package com.checkmyclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링에게 "이거 설정 파일이니까 시작할 때 꼭 읽어!" 라고 알려주는 역할
public class SecurityConfig {

    // 🌟 여기가 핵심! 스프링 창고에 BCrypt 암호화 기계를 하나 딱 만들어 두는 거야.
    // 이제 UserService가 달라고 하면 스프링이 이걸 쓱 꺼내서 줌!
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🌟 (필수) 시큐리티가 허락 없이 자기들만의 기본 로그인 창을 띄우는 걸 막고,
    // 우리가 만든 예쁜 HTML 화면을 쓸 수 있게 모든 길을 열어주는 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 폼 전송 테스트를 위해 우선 CSRF 방어 잠시 해제
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 일단 모든 주소 접근 허용! (나중에 권한별로 막을 수 있음)
                );
        return http.build();
    }
}