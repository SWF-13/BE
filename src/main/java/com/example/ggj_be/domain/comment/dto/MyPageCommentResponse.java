package com.example.ggj_be.domain.comment.dto;

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
        this.reply_id = reply.getReply_id();
        this.createdAt = reply.getCreated_at();
        this.updatedAt = reply.getUpdated_at();
        this.userId = reply.getMember().getUserSeq();
        this.content = reply.getContent();
    }
}
