package com.example.ggj_be.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class BoardHomeListResponse {
    private List<BoardSelecHomeListRequest> homeList;
    private List<BoardSelecHomeListRequest> competitionList;
}