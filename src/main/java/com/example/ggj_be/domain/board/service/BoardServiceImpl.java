package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.reply.Reply;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.reply.repository.ReplyRepository;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.common.repository.PotoRepository;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardHomeList;
import com.example.ggj_be.domain.board.dto.BoardDetail;
import com.example.ggj_be.domain.member.Member;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDateTime;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PotoRepository potoRepository;

    @Override
    public Long createBoard(BoardCreateRequest request) {



        try{
            Member member = memberRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            Board board = Board.builder()
            .categoryId(request.getCategoryId())
            .member(member)
            .title(request.getTitle())
            .content(request.getContent())
            .boardPrize(request.getBoardPrize())
            .endAt(request.getEndAt())
            .build();
    
            boardRepository.save(board);
            return board.getBoardId();
        }catch (Exception e){
            log.error(" Error creating board", e);
            throw new RuntimeException("게시글 생성 실패", e);
        }
    }

    @Override
    public List<BoardHomeList> getBoardHomeList(Long userId, int listTpe) {
        List<BoardHomeList> boards = boardRepository.findBoardHomeList(userId, listTpe);
        Comparator<BoardHomeList> comparator;
        if (listTpe == 1) {
            comparator = Comparator.comparingInt(BoardHomeList::getEndCount); // 마감일 오름차순
        } else if (listTpe == 2) {
            comparator = Comparator.comparingInt(BoardHomeList::getGoodCount).reversed(); // 좋아요 내림차순
        } else if (listTpe == 3) {
            comparator = Comparator.comparingLong(BoardHomeList::getBoardPrize).reversed(); // 상금 내림차순
        } else if (listTpe == 4) {
            comparator = Comparator.comparingInt(BoardHomeList::getReplyCount).reversed(); // 댓글 내림차순
        } else {
            comparator = Comparator.comparing(BoardHomeList::getCreatedAt).reversed(); // 최신 게시글 내림차순
        }

        boards.sort(comparator);

        // listTpe이 5일 경우 limit을 적용하지 않음
        if (listTpe == 5) {
            return boards; // limit 없이 전체 리스트 반환
        }

        int limit = (listTpe == 4) ? 10 : 5; // listType이 4이면 10개, 아니면 5개
        return boards.subList(0, Math.min(boards.size(), limit));
    }


    @Override
    public List<BoardHomeList> getSearchBoardList(Long userSeq, String search) {
        List<BoardHomeList> boards = boardRepository.findSearchBoardList(userSeq, search);
        Comparator<BoardHomeList> comparator;
        comparator = Comparator.comparing(BoardHomeList::getCreatedAt).reversed(); // 최신 게시글 내림차순
        boards.sort(comparator);

        return boards;
    }

    @Override
    public List<BoardHomeList> getCategoryBoardList(Long userSeq, int CategoryId) {
        List<BoardHomeList> boards = boardRepository.findSearchBoardList(userSeq, CategoryId);
        Comparator<BoardHomeList> comparator;
        comparator = Comparator.comparing(BoardHomeList::getCreatedAt).reversed(); // 최신 게시글 내림차순
        boards.sort(comparator);

        return boards;
    }

    @Override
    public BoardDetail getBoardDetail(Long userId, Long boardId) {
        BoardDetail boardDetail = boardRepository.findBoardDetail(userId, boardId);
        return boardDetail;
    }

    @Override
    public List<Poto> getImages(Type type, Long objectId) {
        List<Poto> boardImages = potoRepository.findByTypeAndObjectId(type, objectId);
        return boardImages;
    }

    @Override
    public Boolean boardDelete(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            boardRepository.deleteById(boardId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean boardAccAtUdate(Long boardId, Long replyId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        if (boardOptional.isPresent() && replyOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setAccAt(LocalDateTime.now());
            boardRepository.save(board);

            Reply reply = replyOptional.get();
            reply.setAccAt(LocalDateTime.now());
            replyRepository.save(reply);
            return true;
        } else {
            return false;
        }
    }


    
}
