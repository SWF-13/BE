package com.example.ggj_be.domain.reply.dto;

import com.example.ggj_be.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPageCommentResponse {
    private Long reply_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String content;

    @Builder
    public MyPageCommentResponse(Reply reply){
        this.reply_id = reply.getReplyId();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
        this.userId = reply.getMember().getUserId();
        this.content = reply.getContent();
    }
}
