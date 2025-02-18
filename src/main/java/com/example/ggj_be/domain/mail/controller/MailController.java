package com.example.ggj_be.domain.mail.controller;

import com.example.ggj_be.domain.mail.service.MailService;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Email 관련 API 명세서", description = "인증 코드 전송, 검증 처리하는 API")
@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @Operation(summary = "이메일 인증 코드 전송", description = "비밀번호 변경 시 이메일 인증 단계가 필요함 / 인증코드 숫자 6자리를 전송합니다.")
    @PostMapping
    public ApiResponse<String> sendAuthCode(@RequestBody @Valid MemberRequest.SendEmail request)
            throws MessagingException {

        mailService.sendEmailMessage(request.getEmail());

        return ApiResponse.onSuccess("성공적으로 인증코드가 전송되었습니다.");
    }

    @Operation(summary = "이메일 인증 코드 검증", description = "전송받은 인증코드 6자리와 서버에서 관리하는 6자리를 비교합니다.")
    @PostMapping("/verify")
    public ApiResponse<String> verifyCode(@RequestBody @Valid MemberRequest.VerifyCode request) {

        return ApiResponse.onSuccess(mailService.verifyAuthCode(request));
    }
}
