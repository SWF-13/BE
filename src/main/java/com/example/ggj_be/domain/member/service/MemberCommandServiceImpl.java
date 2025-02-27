package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.common.CustomResult;
import com.example.ggj_be.domain.enums.Bank;
import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

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
    public String changePassword(Member member, String newPassword) {

        String encodePassword = bCryptEncoder.encode(newPassword);
        Member existMember = memberQueryService.findMember(member);

        existMember.changePassword(encodePassword);

        return encodePassword;
    }

    @Override
    public MemberRequest.ChangeNickName changeNickName(Member member, MemberRequest.ChangeNickName request) {
        member.changeNickName(request.getNickName());
        memberRepository.save(member);
        log.info("change nickname to " + request.getNickName());
        return request;
    }


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

    @Override
    public Member addBankInfo(Long userId, BankRequest.BankRequestDto request) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 은행 코드로부터 enum을 찾습니다.
        Bank bank = Bank.fromCode(request.getBankCode());

        // Bank enum을 사용하여 업데이트
        Member updatedMember = member.toBuilder()
                .bankAccount(request.getBankAccount())
                .bankName(bank)  // Enum으로 저장
                .build();

        return memberRepository.save(updatedMember);
    }


}