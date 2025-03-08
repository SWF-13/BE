package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.common.CustomResult;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {
    String changePassword(Member member, String newPassword);

    Member signUp(SignUpRequest request);
    Member addBankInfo(Long userId, BankRequest.BankRequestDto request);
    String changeNickName(Member member, String request);
    MemberRequest.Mypage getMyInfo(Member member);
}
