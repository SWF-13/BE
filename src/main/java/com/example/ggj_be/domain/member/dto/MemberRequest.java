package com.example.ggj_be.domain.member.dto;

import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

public class MemberRequest {

    @Schema(description = "비밀번호 변경 요청 DTO")
    @Getter
    public static class ChangePassword {


        @Schema(description = "회원의 비밀번호", example = "123456")
        @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
        private String password;
    }


    @Schema(description = "이메일 인증 요청 DTO")
    @Getter
    public static class AuthAccountId {

        @Schema(description = "회원의 아이디", example = "swfswf1")
        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;
    }


    @Schema(description = "이메일 인증 코드 검증 DTO")
    @Getter
    public static class VerifyCode {

        @Schema(description = "회원의 아이디", example = "swfswf1@example.com")

        @NotBlank(message = "아이디가 입력되지 않았습니다.")
        private String accountId;

        @Schema(description = "이메일로부터 받은 인증 코드", example = "123456")
        @Pattern(regexp = "\\d{6}", message = "인증 코드는 6자리 숫자여야 합니다.")
        private String authCode;
    }

    public static Member toEntity(String accountId, String password) {

        return Member.builder()
                .nameKo("MEMBER_1")
                .accountid(accountId)
                .password(password)
                .memberNo("12345678")
                .joinDt(LocalDate.of(2024, 7, 1))
                .role(Role.MEMBER)
                .build();
    }
}