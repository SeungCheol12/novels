package com.example.novels.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Setter
public class RegisterDTO {
    // 회원가입용
    @NotBlank
    @Email(message = "이메일 확인")
    private String email;

    @NotBlank(message = "비밀번호 확인")
    private String password;

    @NotBlank(message = "이름 확인")
    private String nickname;

    private boolean fromSocial;

}
