package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.enums.SortType;
import com.example.ggj_be.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardCommandServiceImpl implements BoardCommandService {

    private final BoardRepository boardRepository;

    @Override
    public List<MyPageBoardResponse> getMyBoards(Member member, SortType sortType) {
        Long userId = member.getUserId();
        List<Board> boards = boardRepository.findByMember_UserId(userId);

        // Board -> DTO 변환 후 정렬
        List<MyPageBoardResponse> boardList = boards.stream()
                .map(MyPageBoardResponse::new)
                .sorted(getComparator(sortType)) // 정렬 적용
                .collect(Collectors.toList());

        return boardList;
    }

    private Comparator<MyPageBoardResponse> getComparator(SortType sortType) {
        switch (sortType) {
            case DEADLINE: // 마감 임박순 (endAt이 가까운 순)
                return Comparator.comparing(MyPageBoardResponse::getDaysUntilEnd);
            case LIKES: // 좋아요 많은 순 (goodsCount 내림차순)
                return Comparator.comparing(MyPageBoardResponse::getGoodsCount).reversed();
            case PRIZE: // 상금 높은 순 (boardPrize 내림차순)
                return Comparator.comparingLong(MyPageBoardResponse::getBoardPrize).reversed();
            default:
                return Comparator.comparing(MyPageBoardResponse::getCreatedAt).reversed(); // 기본값: 최신순
        }
    }
}
