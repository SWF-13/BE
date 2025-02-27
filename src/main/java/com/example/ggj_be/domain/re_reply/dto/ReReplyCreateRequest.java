package com.example.ggj_be.domain.re_reply.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReReplyCreateRequest {
    private Long userId;        //나중에 삭제해야함
    private Long replyId;
    private String content;
}
