package com.example.ggj_be.domain.reply.service;

import com.example.ggj_be.domain.reply.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.member.Member;

import java.util.List;

public interface ReplyCommandService {
    List<MyPageCommentResponse> getMyComments(Member member);
}
