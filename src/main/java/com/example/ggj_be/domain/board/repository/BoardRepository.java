package com.example.ggj_be.domain.board.repository;

import com.example.ggj_be.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMember_UserId(Long userId);
}
