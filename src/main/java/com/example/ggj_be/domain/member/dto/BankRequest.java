package com.example.ggj_be.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class BankRequest {
    @Schema(description = "계좌 등록 요청 DTO")
    @Getter
    public static class BankRequestDto {
        @Schema(description = "회원의 은행", example = "국민은행")
        @NotBlank(message = "은행이 입력되지 않았습니다.")
        private String bankName;

        @Schema(description = "회원의 계좌번호", example = "3333-00-0000000")
        @NotBlank(message = "계좌번호가 입력되지 않았습니다.")
        private String bankAccount;
    }
}
