package com.example.ggj_be.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCreateRequest {

    private long category_id;
    private long user_seq;
    private String title;
    private String content;
    private int board_prize;
    private LocalDateTime end_at;

}