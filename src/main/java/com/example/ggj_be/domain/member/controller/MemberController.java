package com.example.ggj_be.domain.member.controller;


import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.service.BoardCommandService;
import com.example.ggj_be.domain.enums.Bank;
import com.example.ggj_be.domain.member.service.MemberQueryService;
import com.example.ggj_be.domain.reply.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.reply.service.ReplyCommandService;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.member.service.MemberCommandService;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.service.ScrapCommandService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.ApiResponse;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import com.example.ggj_be.global.s3.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final BoardCommandService boardCommandService;
    private final ReplyCommandService replyCommandService;
    private final ScrapCommandService scrapCommandService;
    private final MemberCommandService memberCommandService;
    private final FileStorageService fileStorageService;
    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;


    @GetMapping("/myboardlist")
    public ApiResponse<List<MyPageBoardResponse>> getMyBoards(@AuthMember Member member) {
        List<MyPageBoardResponse> boardList = boardCommandService.getMyBoards(member);
        return ApiResponse.onSuccess(boardList);
    }

    @GetMapping("/mycommentlist")
    public ApiResponse<List<MyPageCommentResponse>> getMyComments(@AuthMember Member member) {
        List<MyPageCommentResponse> commentList = replyCommandService.getMyComments(member);
        return ApiResponse.onSuccess(commentList);
    }
    @GetMapping("/scraplist")
    public ApiResponse<List<ScrapDto>> getScraps(@AuthMember Member member) {
        List<ScrapDto> scrapList = scrapCommandService.getScraps(member);
        return ApiResponse.onSuccess(scrapList);
    }
    @Operation(summary = "은행 정보 등록 및 변경")
    @PatchMapping("/bank-info")
    public ApiResponse<Member> bankInfo(@AuthMember Member member, @RequestBody BankRequest.BankRequestDto request) {
        Member updatedMember = memberCommandService.addBankInfo(member.getUserId(), request);
        return ApiResponse.onSuccess(updatedMember);
    }

    @Operation(summary = "닉네임 변경")
    @PatchMapping("/changeNickname")
    public ApiResponse<String> changeNickname(@AuthMember Member member, @RequestBody MemberRequest.ChangeNickName request) {
        String changeNickName = memberCommandService.changeNickName(member, request.getNickName());
        return ApiResponse.onSuccess(changeNickName);
    }


    @Operation(summary = "프로필 사진 변경")
    @PatchMapping("/upload-img")
    public ResponseEntity<String> uploadProfileImage(@AuthMember Member member,@RequestParam("file") MultipartFile file) {
        try {
            // 파일 이름을 UUID로 설정하여 중복을 방지
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();


            // 네이버 클라우드에 파일 업로드 (여기서 fileStorageService는 NCP와의 연결을 담당)
            String imageUrl = fileStorageService.uploadProfileImage(file, fileName);

            // 업로드된 이미지 URL을 Member 엔티티에 반영
            member.updateProfileImage(imageUrl);  // Member 엔티티의 userImg 필드 업데이트

            memberRepository.save(member);



            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드에 실패했습니다.");
        }
    }

    @Operation(summary = "기본 프로필 이미지 선택 및 닉네임 설정")
    @PatchMapping("/ImgAndNickname")
    public ApiResponse<MemberRequest.NickNameAndImg> updateProfileImageWithDefault(@AuthMember Member member, @RequestBody MemberRequest.NickNameAndImg request) {

            List<String> defaultImageUrls = Arrays.asList(
                    "https://kr.object.ncloudstorage.com/profile-img/basic/6C7CC82D-CA70-4894-968B-44A344A85C94.png",
                    "https://kr.object.ncloudstorage.com/profile-img/basic/9957A943-220E-4BF1-B319-F3BA8D122599.png",
                    "https://kr.object.ncloudstorage.com/profile-img/basic/B9CBB4D7-18A0-49BC-84A1-E0D5EC1F8112.png",
                    "https://kr.object.ncloudstorage.com/profile-img/basic/EEA39F71-0CA1-4FCA-A0DE-090EB3956767.png"
            );
            log.info("imageUrl:{}", request.getImgUrl());

            if (!defaultImageUrls.contains(request.getImgUrl())) {
                return ApiResponse.onFailure(ErrorStatus._IMG_NOT_FOUND.getCode(), ErrorStatus._IMG_NOT_FOUND.getMessage(), null);
            }

            member.updateProfileImage(request.getImgUrl());
            log.info("image uploaded successfully");
            memberRepository.save(member);

            //닉네임
            String changeNickName = memberCommandService.changeNickName(member, request.getNickName());
            log.info("changeNickName: " + changeNickName);

            MemberRequest.NickNameAndImg nickNameAndImg = new MemberRequest.NickNameAndImg(changeNickName, request.getImgUrl());

            return ApiResponse.onSuccess(nickNameAndImg);

    }



    @Operation(summary = "(이메일 인증 코드 검증 후) 회원의 비밀번호를 변경합니다.", description = "JWT을 Header에 담아서 요청해야합니다.")
    @PatchMapping("/changePassword")
    public ApiResponse<String> changePassword(@AuthMember Member member,
                                                    @RequestBody @Valid MemberRequest.ChangePassword request) {

        return ApiResponse.onSuccess(
                memberCommandService.changePassword(member, request.getPassword()));

    }

    @Operation(summary = "마이페이지에서 닉네임과 캐시를 가져옵니다.")
    @GetMapping("/profile")
    public ApiResponse<MemberRequest.Mypage> getProfile(@AuthMember Member member) {
        Long point = member.getPoint();
        String nickName = member.getNickName();
        boolean isResistered;

        if(member.getBankAccount()==null){
            isResistered = false;
        }
        else {
            isResistered = true;
        }

        // Mypage 객체 생성 후 반환
        MemberRequest.Mypage mypage = new MemberRequest.Mypage(point, nickName, member.getUserImg(), isResistered);


        return ApiResponse.onSuccess(mypage);
    }

    @Operation(summary = "계정 탈퇴 api")
    @DeleteMapping("/deleteMember")
    public ApiResponse<String> deleteMember(@AuthMember Member member) {
        memberQueryService.deleteMember(member);
        return ApiResponse.onSuccess("성공적으로 계정이 탈퇴되었습니다.");
    }

    @Operation(summary = "회원의 은행정보 가져오기 api")
    @GetMapping("/bank-info")
    public ApiResponse<BankRequest.BankResponseDto> getBankInfo(@AuthMember Member member) {

        if(member.getBankAccount()==null){
            Bank bank = Bank.fromCode("000");
            BankRequest.BankResponseDto bankResponsetDto = new BankRequest.BankResponseDto(bank.getCode(), "등록된 계좌번호가 없습니다.");

            return ApiResponse.onSuccess(bankResponsetDto);
        }
        else {
            BankRequest.BankResponseDto bankResponsetDto = new BankRequest.BankResponseDto(member.getBankName().getCode(), member.getBankAccount());
            log.info("bankResponsetDto: {}", bankResponsetDto);
            return ApiResponse.onSuccess(bankResponsetDto);
        }

    }



}
