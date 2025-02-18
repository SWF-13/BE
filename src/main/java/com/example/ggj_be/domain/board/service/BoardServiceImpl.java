package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardSelectEndRequest;
import com.example.ggj_be.global.response.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;




@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Long createBoard(BoardCreateRequest request) {
        try{
            
            Board board = Board.builder()
            .category_id(request.getCategory_id())
            .member(request.getMember())
            .title(request.getTitle())
            .content(request.getContent())
            .board_prize(request.getBoard_prize())
            .end_at(request.getEnd_at())
            .build();
    
            boardRepository.save(board);
            return board.getBoard_id();
        }catch (Exception e){
            log.error(" Error creating board", e);
            throw new RuntimeException("게시글 생성 실패", e);
        }
    }

    @Override
    public List<BoardSelectEndRequest> getBoardSelectEndRequests() {
        return boardRepository.findBoardSelectEndRequest();
    }
    
}
