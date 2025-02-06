package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.common.CustomResult;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;

    @Override
    public CustomResult changePassword(Member member, String newPassword) {

        String encodePassword = bCryptEncoder.encode(newPassword);
        Member existMember = memberQueryService.findMember(member);
        existMember.changePassword(encodePassword);

        return CustomResult.toCustomResult(Long.valueOf(existMember.getAccountid()));
    }

    @Override
    public Member signUp(SignUpRequest request) {

        if (memberRepository.findByAccountid(request.getAccountId()).isPresent()) {
            throw new ApiException(ErrorStatus._MEMBER_DUPLICATED_ID);
        }

        Member member = MemberRequest.toEntity(request.getAccountId(),
                bCryptEncoder.encode(request.getPassword()));

        return memberRepository.save(member);
    }
}