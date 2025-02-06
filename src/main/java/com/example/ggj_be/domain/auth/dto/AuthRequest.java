package com.example.ggj_be.domain.auth.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class AuthRequest {

    @Schema(description = " 로그인 요청 DTO")
    @Getter
    public static class LoginRequest{
        @Schema(description = "로그인 아이디(이메일)", example = "example@mail.com")
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;

        @Schema(description = "로그인 비밀번호", example = "1234")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }
}
