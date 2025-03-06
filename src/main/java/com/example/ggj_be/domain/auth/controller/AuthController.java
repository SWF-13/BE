package com.example.ggj_be.domain.auth.controller;

import com.example.ggj_be.domain.auth.AuthService;
import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.auth.dto.TokenVo;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.service.MemberCommandService;
import com.example.ggj_be.domain.member.service.MemberQueryService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Auth 관련 API 명세서", description = "로그인, 로그아웃, 토큰 갱신 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;
    private final AuthService authService;

    @Operation(summary = "사용자/관리자 로그인 API", description = "일반 직원인 경우 EMPLOYEE, 관리자인 경우 ADMIN을 반환합니다.")
    @PostMapping("/login")
    public ApiResponse<AuthRequest.LoginResponse> login(@RequestBody AuthRequest.LoginRequest request) {

        Member member = memberQueryService.checkAccountIdAndPwd(request);
        TokenVo tokenVo = authService.generateATAndRT(member);
        AuthRequest.LoginResponse loginResponse = new AuthRequest.LoginResponse(tokenVo, member.getUserId());

        return ApiResponse.onSuccess(loginResponse);
    }

    @Operation(summary = "로그아웃 및 블랙리스트 관리 API", description = "보안을 위해 로그아웃 시 AccessToken을 블랙리스트에 관리합니다.")
    @PostMapping("/logout")
    public ApiResponse<String> logout(@AuthMember Member member, HttpServletRequest request) {

        authService.logout(request);

        return ApiResponse.onSuccess("성공적으로 로그아웃되었습니다.");
    }


    @Operation(summary = "회원가입 API", description = "새로운 사용자를 등록합니다.")
    @PostMapping("/signup")
    public ApiResponse<Member> register(@RequestBody SignUpRequest request) {
        Member registeredMember = memberCommandService.signUp(request);
        return ApiResponse.onSuccess(registeredMember);
    }
}
