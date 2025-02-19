package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardSelecHomeListRequest;
import com.example.ggj_be.domain.member.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Comparator;




@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;  // Repository 주입

    @Override
    public Long createBoard(BoardCreateRequest request) {



        try{
            
            Member member = memberRepository.findById(request.getUser_seq())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Board board = Board.builder()
            .category_id(request.getCategory_id())
            .member(member)
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
    public List<BoardSelecHomeListRequest> getBoardSelectHomeListRequests(Long user_seq, int list_type) {
        List<BoardSelecHomeListRequest> boards = boardRepository.findBoardSelectEndRequest(user_seq, list_type);
        Comparator<BoardSelecHomeListRequest> comparator;
        if (list_type == 1) {
            comparator = Comparator.comparingInt(BoardSelecHomeListRequest::getEndCount); // 마감일 오름차순
        } else if (list_type == 2) {
            comparator = Comparator.comparingInt(BoardSelecHomeListRequest::getGoodCount).reversed(); // 좋아요 내림차순
        } else if (list_type == 3) {
            comparator = Comparator.comparingLong(BoardSelecHomeListRequest::getBoardPrize).reversed(); // 상금 내림차순
        } else {
            comparator = Comparator.comparingInt(BoardSelecHomeListRequest::getReplyCount).reversed(); // 댓글 내림차순
        }

        boards.sort(comparator);

        int limit = (list_type == 4) ? 10 : 5; // listType이 4이면 10개, 아니면 5개
        return boards.subList(0, Math.min(boards.size(), limit));
    }
    
}
