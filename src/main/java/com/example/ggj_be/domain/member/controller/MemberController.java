package com.example.ggj_be.domain.member.controller;


import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.service.BoardCommandService;
import com.example.ggj_be.domain.comment.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.comment.service.CommentCommandService;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;
import com.example.ggj_be.domain.member.dto.MemberRequest;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.member.service.MemberCommandService;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.service.ScrapCommandService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import com.example.ggj_be.global.s3.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final BoardCommandService boardCommandService;
    private final CommentCommandService commentCommandService;
    private final ScrapCommandService scrapCommandService;
    private final MemberCommandService memberCommandService;
    private final FileStorageService fileStorageService;
    private final MemberRepository memberRepository;


    @GetMapping("/myboardlist")
    public ApiResponse<List<MyPageBoardResponse>> getMyBoards(@AuthMember Member member) {
        List<MyPageBoardResponse> boardList = boardCommandService.getMyBoards(member);
        return ApiResponse.onSuccess(boardList);
    }

    @GetMapping("/mycommentlist")
    public ApiResponse<List<MyPageCommentResponse>> getMyComments(@AuthMember Member member) {
        List<MyPageCommentResponse> commentList = commentCommandService.getMyComments(member);
        return ApiResponse.onSuccess(commentList);
    }
    @GetMapping("/scraplist")
    public ApiResponse<List<ScrapDto>> getScraps(@AuthMember Member member) {
        List<ScrapDto> scrapList = scrapCommandService.getScraps(member);
        return ApiResponse.onSuccess(scrapList);
    }
    @Operation(summary = "은행 정보 등록")
    @PatchMapping("/bank-info")
    public ApiResponse<Member> bankInfo(@AuthMember Member member, @RequestBody BankRequest.BankRequestDto request) {
        Member updatedMember = memberCommandService.addBankInfo(member.getUserId(), request);
        return ApiResponse.onSuccess(updatedMember);
    }

    @Operation(summary = "닉네임 변경")
    @PatchMapping("/changeNickname")
    public ApiResponse<MemberRequest.ChangeNickName> changeNickname(@AuthMember Member member, @RequestBody MemberRequest.ChangeNickName request) {
        MemberRequest.ChangeNickName changeNickName = memberCommandService.changeNickName(member, request);
        return ApiResponse.onSuccess(changeNickName);
    }


    @Operation(summary = "프로필 사진 등록 및 변경")
    @PatchMapping("/upload-img")
    public ResponseEntity<String> uploadProfileImage(@AuthMember Member member, @RequestParam("file") MultipartFile file) {
        try {
            // 파일 이름을 UUID로 설정하여 중복을 방지
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();


            // 네이버 클라우드에 파일 업로드 (여기서 fileStorageService는 NCP와의 연결을 담당)
            String imageUrl = fileStorageService.uploadProfileImage(file, fileName);

            // 업로드된 이미지 URL을 Member 엔티티에 반영
            member.updateProfileImage(imageUrl);  // Member 엔티티의 userImg 필드 업데이트

            memberRepository.save(member);

            // 업로드된 이미지 URL을 응답으로 반환
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("파일 업로드에 실패했습니다.");
        }
    }



}
