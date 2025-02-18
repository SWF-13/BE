package com.example.ggj_be.domain.board.dto;

import com.example.ggj_be.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCreateRequest {

    private long category_id;
    private Member member;
    private String title;
    private String content;
    private int board_prize;
    private LocalDateTime end_at;

}