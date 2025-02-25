package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.member.Member;

import java.util.List;

public interface BoardCommandService {
    List<MyPageBoardResponse> getMyBoards(Member member);
}
