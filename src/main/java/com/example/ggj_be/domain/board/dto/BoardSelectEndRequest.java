package com.example.ggj_be.domain.board.dto;

public interface BoardSelectEndRequest {
    String getCategoryName();
    String getTitle();
    int getGoodCount();
    int getReplyCount();
    int getEndCount();
}
