package com.example.ggj_be.domain.re_reply.service;

import com.example.ggj_be.domain.re_reply.dto.ReReplyCreateRequest;

import java.util.List;


public interface ReReplyService {
    Boolean createReReply(Long userId, ReReplyCreateRequest request);

}
