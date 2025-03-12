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

    @Schema(description = "비밀번호 변경 및 분실 응답 DTO")
    @Getter
    public static class ChangedPassword{
        private String newPassword;
    }

    @Schema(description = "이메일 DTO")
    @Getter
    public static class SendEmail {
        @Schema(description = "회원의 이메일", example = "example@example.com")
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        private String email;
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

    @Schema(description = "닉네임 변경 DTO")
    @Getter
    public static class ChangeNickName {
        @Schema(description = "회원이 변경하고자 하는닉네임", example = "updatedNick")
        @NotBlank(message = "닉네임이 입력되지 않았습니다.")
        private String nickName;

    }

    @Schema(description = "비밀번호 발송 이메일 DTO")
    @Getter
    public static class Email {
        @NotBlank(message = "이메일이 입력되지 않았습니다.")
        private String email;
    }

    @Schema(description = "마이페이지 닉네임 및 캐시")
    @Getter
    public static class Mypage{
        private Long point;
        private String nickName;
        private String imgUrl;
        private boolean isResisterBank;

        // 생성자 추가
        public Mypage(Long point, String nickName, String imgUrl, boolean isResisterBank) {
            this.point = point;
            this.nickName = nickName;
            this.imgUrl = imgUrl;
            this.isResisterBank = isResisterBank;
        }
    }

    @Schema(description = "마이페이지 닉네임 및 기본 프로필 이미지")
    @Getter
    public static class NickNameAndImg {
        private String nickName;
        private String imgUrl;

        // 기본 생성자 추가 (Jackson 직렬화/역직렬화용)
        public NickNameAndImg() {}

        public NickNameAndImg(String changeNickName, String imageUrl) {
            this.nickName = changeNickName;
            this.imgUrl = imageUrl;
        }
    }



    public static Member toEntity(String accountId, String password) {

        return Member.builder()
                .nameKo("MEMBER_1")
                .accountid(accountId)
                .password(password)
//                .memberNo("12345678")
                .joinDt(LocalDate.of(2024, 7, 1))
                .role(Role.MEMBER)
                .build();
    }
}