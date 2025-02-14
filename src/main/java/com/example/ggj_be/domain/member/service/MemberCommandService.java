package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.common.CustomResult;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;

public interface MemberCommandService {
    CustomResult changePassword(Member member, String newPassword);

    Member signUp(SignUpRequest request);
    Member addBankInfo(Long userId, BankRequest.BankRequestDto request);
}
