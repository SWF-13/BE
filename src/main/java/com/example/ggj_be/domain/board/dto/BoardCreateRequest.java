package com.example.ggj_be.domain.board.dto;

import com.example.ggj_be.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardCreateRequest {

    private Long categoryId;
    private String title;
    private String content;
    private Long boardPrize;
    private LocalDateTime endAt;

}