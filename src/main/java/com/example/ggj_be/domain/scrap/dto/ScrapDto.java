package com.example.ggj_be.domain.scrap.dto;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.scrap.Scrap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScrapDto {
    private Long id;
    private Board board;
    private Long userId;
    private LocalDateTime scrap_createdAt;
    private String title;
    private LocalDateTime board_created_at;

    @Builder
    public ScrapDto(Scrap scrap) {
        this.id = scrap.getId();
        this.board= scrap.getBoard();
        this.userId = scrap.getMember().getUserId();
        this.scrap_createdAt = scrap.getCreatedAt();
        this.title = scrap.getBoard().getTitle();
        this.board_created_at = scrap.getBoard().getCreated_at();
    }
}
