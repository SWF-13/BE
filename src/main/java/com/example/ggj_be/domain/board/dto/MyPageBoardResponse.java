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

    @Builder
    public MyPageBoardResponse(Board board, int goodsCount) {
        this.boardId = board.getBoardId();
        this.category = String.valueOf(board.getCategoryId());
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.userId = board.getMember().getUserId();
        this.goodsCount = goodsCount; // 수정된 부분
        log.info("board good count: {}", goodsCount);
        this.replyCount = board.getReplies().size();
        log.info("board reply count: {}", replyCount);
        this.daysUntilEnd = calculateDaysUntilEnd(board.getEndAt());
    }

    private long calculateDaysUntilEnd(LocalDateTime endAt) {
        if (endAt == null) {
            return -1; // 마감일이 없으면 -1 반환
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endAt);
    }
}
