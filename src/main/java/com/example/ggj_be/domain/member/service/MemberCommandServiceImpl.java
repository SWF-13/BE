package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.common.CustomResult;
import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import com.example.ggj_be.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberCommandServiceImpl implements MemberCommandService {

    private final BCryptPasswordEncoder bCryptEncoder;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    @Override
    public CustomResult changePassword(Member member, String newPassword) {

        String encodePassword = bCryptEncoder.encode(newPassword);
        Member existMember = memberQueryService.findMember(member);
        existMember.changePassword(encodePassword);


        return CustomResult.toCustomResult(Long.valueOf(existMember.getAccountid()));
    }

//    @Override
//    public Member signUp(SignUpRequest request) {
//
//        if (memberRepository.findByAccountid(request.getAccountId()).isPresent()) {
//            throw new ApiException(ErrorStatus._MEMBER_DUPLICATED_ID);
//        }
//
//        Member member = MemberRequest.toEntity(request.getAccountId(),
//                bCryptEncoder.encode(request.getPassword()));
//
//        return memberRepository.save(member);
//    }


    @jakarta.transaction.Transactional
    public Member signUp(SignUpRequest request) {
        // 이메일 중복 체크
        if (memberRepository.existsByAccountid(request.getAccountId())) {
            throw new ApiException(ErrorStatus._MEMBER_DUPLICATED_ID);
        }
        //닉네임 중복 체크
        if (memberRepository.existsByNickName(request.getNickName())) {
            throw new ApiException(ErrorStatus._MEMBER_DUPLICATED_NICKNAME);
        }

//        // 이메일 인증 코드 확인
//        String storedAuthCode = redisUtil.getEmailCode(request.getAccountId());
//        if (storedAuthCode == null || !storedAuthCode.equals(request.getAuthCode())) {
//            throw new ApiException(ErrorStatus._MAIL_WRONG_CODE);
//        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 회원 저장
        Member member = Member.builder()
                .accountid(request.getAccountId())
                .password(encodedPassword)
                .nickName(request.getNickName())
                .email(request.getEmail())
                .nameKo(request.getNameKo())
//                .memberNo(request.getMemberNo())
                .userBirth(request.getUserBirth())
                .userImg(request.getUserImg())
                .agreeService(request.getAgreeService())
                .agreeInfo(request.getAgreeInfo())
                .role(Role.MEMBER) // 기본 권한 설정
                .joinDt(LocalDate.now())
                .build();

        // 이메일 인증 코드 삭제
        redisUtil.deleteEmailCode(request.getAccountId());

        log.info("member: {}", member);
        return memberRepository.save(member);




    }

}