package com.example.ggj_be.domain.scrap.dto;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.scrap.Scrap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class ScrapDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private LocalDateTime scrap_createdAt;
    private String title;
    private LocalDateTime board_created_at;
    private int goodsCount;
    private int replyCount;
    private long daysUntilEnd;
    private String category;

    @Builder
    public ScrapDto(Scrap scrap) {
        this.id = scrap.getScrapId();
        this.boardId= scrap.getBoard().getBoardId();
        this.userId = scrap.getMember().getUserId();
        this.scrap_createdAt = scrap.getCreatedAt();
        this.title = scrap.getBoard().getTitle();
        this.board_created_at = scrap.getBoard().getCreatedAt();
        this.goodsCount = scrap.getBoard().getGoods().size();
        this.replyCount = scrap.getBoard().getReplies().size();
        this.daysUntilEnd = calculateDaysUntilEnd(scrap.getBoard().getEndAt());
        this.category = String.valueOf(scrap.getBoard().getCategoryId());
    }

    private long calculateDaysUntilEnd(LocalDateTime endAt) {
        if (endAt == null) {
            return -1; // 마감일이 없으면 -1 반환
        }
        return ChronoUnit.DAYS.between(LocalDateTime.now(), endAt);
    }
}
