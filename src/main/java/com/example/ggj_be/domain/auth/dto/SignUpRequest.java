package com.example.ggj_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotBlank(message = "아이디를 입력해주세요. (이메일 형식)")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String accountId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String nameKo;

    @Schema(description = "이메일 인증 코드", example = "123456")
    @NotBlank(message = "이메일 인증 코드가 필요합니다.")
    private String authCode;
}