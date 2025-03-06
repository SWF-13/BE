package com.example.ggj_be.domain.board.service;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardHomeList;
import com.example.ggj_be.domain.board.dto.BoardDetail;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.enums.Type;
import java.util.List;


public interface BoardService {
    Long createBoard(Long userId, BoardCreateRequest request);
    List<BoardHomeList> getBoardHomeList(Long userId, int listType);
    List<BoardHomeList> getSearchBoardList(Long userId, String search);
    List<BoardHomeList> getCategoryBoardList(Long userId, int categoryId);
    BoardDetail getBoardDetail(Long userId, Long boardId);
    List <Poto>getImages(Type type, Long objectId);
    Boolean boardDelete(Long userId, Long boardId);
    Boolean boardAccAtUdate(Long boardId, Long replyId);
    Boolean chkUser(Long boardId, Long userId);
    List<Poto> getImageName(Long objectId, Type type);

}
