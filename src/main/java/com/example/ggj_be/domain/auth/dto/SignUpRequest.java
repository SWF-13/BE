package com.example.ggj_be.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SignUpRequest {

    @Schema(description = "사용자 이메일 (아이디)", example = "user@example.com")
    @NotBlank(message = "아이디를 입력해주세요. (이메일 형식)")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String accountId;

    @Schema(description = "비밀번호", example = "securepassword123")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank(message = "이름을 입력해주세요.")
    private String nameKo;

    @Schema(description = "전화번호", example = "010-0000-0000")
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String memberNo;

    @Schema(description = "생년월일 (YYYY-MM-DD)", example = "2000-01-01")
    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate userBirth;

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    private String userImg;

    @Schema(description = "서비스 이용 약관 동의 여부", example = "true")
    @NotNull(message = "서비스 이용 약관 동의 여부를 입력해주세요.")
    private Boolean agreeService;

    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    @NotNull(message = "개인정보 처리방침 동의 여부를 입력해주세요.")
    private Boolean agreeInfo;

}