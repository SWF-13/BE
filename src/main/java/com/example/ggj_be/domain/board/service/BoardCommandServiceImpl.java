package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.repository.BoardRepository;
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
    public List<MyPageBoardResponse> getMyBoards(Member member) {
        Long userId = member.getUserId();
        List<Board> boards = boardRepository.findByMember_UserId(userId);

        // Board -> DTO 변환 후 정렬
        List<MyPageBoardResponse> boardList = boards.stream()
                .map(MyPageBoardResponse::new)
                .sorted(Comparator.comparing(MyPageBoardResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return boardList;
    }

}
