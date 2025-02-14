package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import global.exception.ApiException;
import global.response.code.status.ErrorStatus;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    @Cacheable(value = "members", key = "#id")
    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    @Override
    public Member findMember(Member member) {
        return memberRepository.findById(Long.valueOf(member.getAccountid())).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
    }

    @Override
    public Member checkAccountIdAndPwd(AuthRequest.LoginRequest loginRequest) {

        Member member = memberRepository.findByAccountId(
                loginRequest.getAccountId()).orElseThrow(
                () -> new ApiException(ErrorStatus._EMPLOYEE_NOT_FOUND)
        );
        if (!bCryptEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new ApiException(ErrorStatus._AUTH_INVALID_PASSWORD);
        }

        return member;
    }



}