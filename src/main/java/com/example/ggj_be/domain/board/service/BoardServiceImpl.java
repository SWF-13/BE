package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.enums.PointType;
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
    public Long createBoard(Long userId, BoardCreateRequest request) {



        try{
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            if (member.getPoint() < request.getBoardPrize()) {
                return 0L;
            }
            Board board = Board.builder()
            .categoryId(request.getCategoryId())
            .member(member)
            .title(request.getTitle())
            .content(request.getContent())
            .boardPrize(request.getBoardPrize())
            .endAt(request.getEndAt())
            .build();
    
            boardRepository.save(board);

            member.setPoint(request.getBoardPrize(), PointType.remove);
            memberRepository.save(member);
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
        List<BoardHomeList> boards = boardRepository.findCategoryBoardList(userSeq, CategoryId);
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
    public Boolean boardDelete(Long userId, Long boardId) {

        try {
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("board not found"));

            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            
            if(board.getMember().getUserId() == member.getUserId()){
                if(board.getBoardPrize() != 0){
                    member.setPoint(board.getBoardPrize(), PointType.add);
                    memberRepository.save(member);
                }
                
                boardRepository.deleteById(boardId);
            }else{
                return false;
            }
            

            return true;
        }catch (Exception e){
            log.error(" Error boardDelete board", e);
            throw new RuntimeException("게시글 삭제 실패", e);
        }
    }

    @Override
    public Boolean boardAccAtUdate(Long boardId, Long replyId) {
        try{
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("board not found"));

            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));


            Member member = memberRepository.findById(reply.getMember().getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            board.setAccAt(LocalDateTime.now());
            boardRepository.save(board);

            reply.setAccAt(LocalDateTime.now());
            replyRepository.save(reply);

            member.setPoint(board.getBoardPrize(), PointType.add);

            return true;
        }catch (Exception e){
            log.error(" Error boardAccAtUdate board", e);
            throw new RuntimeException("채택하기 실패", e);
        }
    }

    @Override
    public Boolean chkUser(Long boardId, Long userId) {
        try{
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new RuntimeException("board not found"));
            if (board.getMember().getUserId().equals(userId)) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            log.error(" Error chkUser board", e);
            throw new RuntimeException("본인이 작성한 게시글인지 찾기 실패", e);
        }
    }

    @Override
    public List<Poto> getImageName(Long objectId, Type type) {
        try{
            List<Poto> imageList = potoRepository.findByObjectIdAndType(objectId, type);
            return imageList;
        }catch (Exception e){
            log.error(" Error chkUser board", e);
            throw new RuntimeException("본인이 작성한 게시글인지 찾기 실패", e);
        }
    }
}
