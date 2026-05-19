package com.checkmyclass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    @NotBlank(message = "이름을 채워주세요.")
    private String userName;

    @NotBlank(message = "학번을 입력해주세요.")
    private String studentNumber;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 4, message = "비밀번호는 4자리 이상이어야 합니다.")
    private String userPassword;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^010\\d{8}$", message = "전화번호는 01000000000 형식의 11자리 숫자여야 합니다.")
    private String phoneNumber;

    @NotBlank(message = "학과를 선택해주세요.")
    private String majorName;

    private String role;
}