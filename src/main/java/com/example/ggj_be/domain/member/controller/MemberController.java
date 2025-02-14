package com.example.ggj_be.domain.member.controller;


import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.service.BoardCommandService;
import com.example.ggj_be.domain.comment.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.comment.service.CommentCommandService;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MemberController {
    private final BoardCommandService boardCommandService;
    private final CommentCommandService commentCommandService;

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

}
