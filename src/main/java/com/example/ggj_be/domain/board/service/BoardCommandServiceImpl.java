package com.example.ggj_be.domain.board.service;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Long userId = member.getUserSeq();// 현재 로그인한 유저 ID 가져오기
        List<Board> boards = boardRepository.findByMember_UserSeq(userId);
        return boards.stream()
                .map(MyPageBoardResponse::new)  // Board → BoardDto 변환
                .collect(Collectors.toList());
    }
}
