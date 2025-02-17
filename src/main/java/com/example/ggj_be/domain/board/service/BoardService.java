package com.example.ggj_be.domain.board.service;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardSelectEndRequest;
import java.util.List;


public interface BoardService {
    Long createBoard(BoardCreateRequest request);
    List<BoardSelectEndRequest> getBoardSelectEndRequests();
}
