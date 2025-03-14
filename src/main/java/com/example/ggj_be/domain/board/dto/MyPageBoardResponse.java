package com.example.ggj_be.domain.board.dto;

import com.example.ggj_be.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Getter
@Slf4j
public class MyPageBoardResponse {
    private Long boardId;
    private String category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private int goodsCount;
    private int replyCount;
    private long daysUntilEnd;
    private int goodChk;

    @Builder
    public MyPageBoardResponse(Board board, int goodsCount, int hasLiked) {
        this.boardId = board.getBoardId();
        this.category = String.valueOf(board.getCategoryId());
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.userId = board.getMember().getUserId();
        this.goodsCount = goodsCount;
        this.replyCount = board.getReplies().size();
        this.daysUntilEnd = calculateDaysUntilEnd(board.getEndAt());
        this.goodChk = hasLiked;
    }

    private long calculateDaysUntilEnd(LocalDateTime endAt) {
        if (endAt == null) {
            return -1;
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endAt);
    }
}
