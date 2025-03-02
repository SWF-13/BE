package com.example.ggj_be.domain.board.dto;

import java.time.LocalDateTime;

public interface BoardHomeList {
    Long getBoardId();
    String getCategoryName();
    Long getCategoryId();
    String getTitle();
    String getNickName();
    int getGoodCount();
    int getReplyCount();
    int getEndCount();
    int getGoodChk();
    Long getBoardPrize();
    LocalDateTime getCreatedAt();
}
