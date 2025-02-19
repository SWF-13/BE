package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptEncoder;

    @Override
    @Cacheable(value = "members", key = "#id")
    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._MEMBER_NOT_FOUND)
        );
    }

    @Override
    public Member findMember(Member member) {
        return memberRepository.findByAccountid(member.getAccountid()).orElseThrow(
                () -> new ApiException(ErrorStatus._MEMBER_NOT_FOUND)
        );
    }

    @Override
    public Member checkAccountIdAndPwd(AuthRequest.LoginRequest loginRequest) {

        Member member = memberRepository.findByAccountid(
                loginRequest.getAccountId()).orElseThrow(
                () -> new ApiException(ErrorStatus._MEMBER_NOT_FOUND)
        );
        if (!bCryptEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new ApiException(ErrorStatus._AUTH_INVALID_PASSWORD);
        }

        return member;
    }



}