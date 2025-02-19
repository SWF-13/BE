package com.example.ggj_be.domain.board.service;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardSelecHomeListRequest;
import java.util.List;


public interface BoardService {
    Long createBoard(BoardCreateRequest request);
    List<BoardSelecHomeListRequest> getBoardSelectHomeListRequests(Long user_seq, int list_type);
}
