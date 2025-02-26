package com.example.ggj_be.domain.reply.service;

import com.example.ggj_be.domain.reply.dto.ReplyCreateRequest;
import com.example.ggj_be.domain.reply.dto.ReplyDetailResponse;

import java.util.List;


public interface ReplyService {
    Long createReply(Long userId, ReplyCreateRequest request);
    List<ReplyDetailResponse> getReplyList(Long userId, Long boardId);

}
