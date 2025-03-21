package com.example.ggj_be.global.response.code.status;

import com.example.ggj_be.global.response.code.BaseErrorCode;
import com.example.ggj_be.global.response.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증되지 않은 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "접근 권한이 없습니다."),


    // 회원 관련
    _MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER400", "요청한 회원 정보를 찾을 수 없습니다.."),
    _MEMBER_DUPLICATED_ID(HttpStatus.BAD_REQUEST, "MEMBER401", "중복된 아이디입니다."),
    _MEMBER_DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "MEMBER402", "중복된 닉네임입니다."),
    _NICKNAME_CANNOTOVER8(HttpStatus.BAD_REQUEST, "MEMBER403", "닉네임은 10자를 초과 할 수 없습니다."),
    _ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404", "등록된 은행 계좌가 없습니다."),
    // 관리자 관련
    _ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN400", "요청한 관리자 정보를 찾을 수 없습니다.."),


    // JWT 관련
    _JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JWT400", "Header에 AccessToken 이 존재하지 않습니다."),
    _JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT401", "검증되지 않는 AccessToken 입니다."),
    _JWT_BLACKLIST(HttpStatus.UNAUTHORIZED, "JWT402", "블랙 리스트에 존재하는 토큰입니다. 다시 로그인 해주세요"),
    _JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "JWT403",
            "Header에 RefreshToken이 존재하지 않습니다."),
    _JWT_DIFF_REFRESH_TOKEN_IN_REDIS(HttpStatus.UNAUTHORIZED, "JWT404",
            "Redis에 존재하는 Refresh Token과 다릅니다."),
    _JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT405", "만료된 AccessToken 입니다."),

    // AUTH 관련

    _AUTH_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH400", "잘못된 비밀번호입니다. 다시 입력해주세요."),

    // Mail 관련
    _MAIL_CREATE_CODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL500",
            "인증 코드 생성 중 서버 에러가 발생했습니다."),
    _MAIL_WRONG_CODE(HttpStatus.BAD_REQUEST, "MAIL400", "올바른 인증코드가 아닙니다."),
    _MAIL_LOTTERY_RESULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MAIL501",
            "추첨 결과 메일 전송 중 서버 에러가 발생했습니다."),
    _USER_DUPLICATE(HttpStatus.BAD_REQUEST, "MAIL401", "중복된 이메일 입니다."),

    //사진 관련
    _IMG_NOT_FOUND(HttpStatus.NOT_FOUND, "IMG400", "이미지를 찾을 수 없습니다."),

    //은행 관련
    _BANK_NOT_FOUND(HttpStatus.NOT_FOUND, "BANK400", "해당하는 은행을 찾을 수 없습니다.");




    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .httpStatus(httpStatus)
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }
}