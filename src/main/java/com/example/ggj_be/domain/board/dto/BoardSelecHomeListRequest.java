package com.example.ggj_be.domain.board.dto;

public interface BoardSelecHomeListRequest {
    Long getBoardId();
    String getCategoryName();
    String getTitle();
    int getGoodCount();
    int getReplyCount();
    int getEndCount();
    int getGoodChk();
    Long getBoardPrize();
}
