package com.example.ggj_be.domain.member.controller;


import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.service.BoardCommandService;
import com.example.ggj_be.domain.comment.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.comment.service.CommentCommandService;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.dto.BankRequest;
import com.example.ggj_be.domain.member.service.MemberCommandService;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.service.ScrapCommandService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MemberController {
    private final BoardCommandService boardCommandService;
    private final CommentCommandService commentCommandService;
    private final ScrapCommandService scrapCommandService;
    private final MemberCommandService memberCommandService;

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

}
