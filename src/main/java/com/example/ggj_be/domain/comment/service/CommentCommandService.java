package com.example.ggj_be.domain.comment.service;

import com.example.ggj_be.domain.comment.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.member.Member;

import java.util.List;

public interface CommentCommandService {
    List<MyPageCommentResponse> getMyComments(Member member);
}
