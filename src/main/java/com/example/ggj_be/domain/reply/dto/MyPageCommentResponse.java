package com.example.ggj_be.domain.reply.dto;

import com.example.ggj_be.domain.reply.Reply;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MyPageCommentResponse {
    private Long reply_id;
    private Long board_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String content;
    private String category;
    private String title;
    private LocalDateTime board_created_at;
    private int goodsCount;
    private int Re_replyCount;
    private long daysUntilEnd;

    @Builder
    public MyPageCommentResponse(Reply reply){
        this.reply_id = reply.getReplyId();
        this.createdAt = reply.getCreatedAt();
        this.updatedAt = reply.getUpdatedAt();
        this.userId = reply.getMember().getUserId();
        this.content = reply.getContent();
        this.category = String.valueOf(reply.getBoard().getCategoryId());
        this.title = reply.getBoard().getTitle();
        this.board_created_at = reply.getBoard().getCreatedAt();
        this.goodsCount = reply.getGoods().size();
        this.Re_replyCount = reply.getRe_replies().size();
        this.daysUntilEnd = calculateDaysUntilEnd(reply.getBoard().getEndAt());
        this.board_id = reply.getBoard().getBoardId();
    }

    private long calculateDaysUntilEnd(LocalDateTime endAt) {
        if (endAt == null) {
            return -1; // 마감일이 없으면 -1 반환
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endAt);
    }
}
