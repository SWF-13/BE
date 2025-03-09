package com.example.ggj_be.domain.member.dto;

import com.example.ggj_be.domain.enums.Bank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class BankRequest {
    @Schema(description = "계좌 등록 요청 DTO")
    @Getter
    public static class BankRequestDto {
        @Schema(description = "회원의 은행 코드", example = "001")
        @NotBlank(message = "은행 코드가 입력되지 않았습니다.")
        private String bankCode;

        @Schema(description = "회원의 계좌번호", example = "3333-00-0000000")
        @NotBlank(message = "계좌번호가 입력되지 않았습니다.")
        private String bankAccount;
    }
    @Schema(description = "회원의 계좌 정보 요청 DTO")
    @Getter
    public static class BankResponseDto {
        private String bankCode;
        private String bankAccount;


        public BankResponseDto(String bankCode, String bankAccount) {
            this.bankCode = bankCode;
            this.bankAccount = bankAccount;
        }
    }
}
