package com.example.ggj_be.global.exception;

import com.example.ggj_be.global.response.ApiResponse;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ApiException을 처리하는 핸들러
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        ErrorStatus errorStatus = e.getErrorStatus();
        ApiResponse<?> apiResponse = ApiResponse.onFailure(
                errorStatus.getCode(),
                errorStatus.getMessage(),
                null
        );
        return new ResponseEntity<>(apiResponse, errorStatus.getHttpStatus());
    }

    // 다른 예외를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception e) {
        ApiResponse<?> apiResponse = ApiResponse.onFailure(
                "INTERNAL_SERVER_ERROR",
                "서버 오류가 발생했습니다. 관리자에게 문의하세요.",
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
