package com.example.ggj_be.domain.board.dto;

import com.example.ggj_be.domain.board.Board;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MyPageBoardResponse {
    private Long boardId;
    private String category;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;

    @Builder
    public MyPageBoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.category = board.getCategory();
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        this.userId = board.getMember().getUserId();  // Member 엔티티에서 userId 가져오기
    }
}
