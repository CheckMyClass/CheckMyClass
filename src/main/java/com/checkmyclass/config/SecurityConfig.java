package com.checkmyclass.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register", "/error").permitAll()
                        .requestMatchers(
                                "/global.css",
                                "/styleguide.css",
                                "/style.css",
                                "/*.png",
                                "/*.jpg",
                                "/*.jpeg",
                                "/*.gif",
                                "/*.webp",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/manager/approve", "/manager/reject", "/manager/update-status")
                        .access((authentication, context) -> new AuthorizationDecision(hasRole(context.getRequest(), "ADMIN")))
                        .requestMatchers("/manager/**")
                        .access((authentication, context) -> new AuthorizationDecision(hasAnyRole(context.getRequest(), "ADMIN", "PROFESSOR")))
                        .anyRequest()
                        .access((authentication, context) -> new AuthorizationDecision(isLoggedIn(context.getRequest())))
                );
        return http.build();
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    private boolean hasRole(HttpServletRequest request, String role) {
        HttpSession session = request.getSession(false);
        return session != null && role.equals(session.getAttribute("role"));
    }

    private boolean hasAnyRole(HttpServletRequest request, String... roles) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        Object currentRole = session.getAttribute("role");
        for (String role : roles) {
            if (role.equals(currentRole)) return true;
        }
        return false;
    }
}
