package com.example.ggj_be.domain.comment.dto;

import com.example.ggj_be.domain.comment.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyPageCommentResponse {
    private Long commentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    @Builder
    public MyPageCommentResponse(Comment comment){
        this.commentId = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.userId = comment.getMember().getUserId();
    }
}
