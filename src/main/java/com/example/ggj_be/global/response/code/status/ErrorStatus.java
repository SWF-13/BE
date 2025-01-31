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


    // 직원 관련
    _MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER400", "요청한 회원 정보를 찾을 수 없습니다.."),
    _MEMBER_DUPLICATED_ID(HttpStatus.BAD_REQUEST, "MEMBER401", "중복된 아이디입니다."),

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
    _AUTH_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH400", "잘못된 비밀번호입니다. 다시 입력해주세요.");



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