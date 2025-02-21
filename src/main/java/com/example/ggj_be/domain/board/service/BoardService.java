package com.example.ggj_be.domain.board.service;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardHomeList;
import com.example.ggj_be.domain.board.dto.BoardDetail;
import com.example.ggj_be.domain.board.dto.ReplyDetailResponse;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.enums.Type;
import java.util.List;


public interface BoardService {
    Long createBoard(BoardCreateRequest request);
    List<BoardHomeList> getBoardHomeList(Long userId, int listType);
    BoardDetail getBoardDetail(Long userId, Long boardId);
    List <Poto>getImages(Type type, Long objectId);
    List<ReplyDetailResponse> getReplyList(Long userId, Long boardId);
}
