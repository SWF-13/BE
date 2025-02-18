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
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;  // 작성자 ID

    @Builder
    public MyPageBoardResponse(Board board) {
        this.boardId = board.getBoard_id();
        this.category = String.valueOf(board.getCategory_id());
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreated_at();
        this.updatedAt = board.getUpdated_at();
        this.userId = board.getMember().getUserSeq();
    }
}
