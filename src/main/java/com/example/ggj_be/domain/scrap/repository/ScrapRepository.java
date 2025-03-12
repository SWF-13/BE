package com.example.ggj_be.domain.scrap.repository;
import com.example.ggj_be.domain.scrap.Scrap;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ScrapRepository extends JpaRepository<Scrap, Long>{
    List<Scrap> findByMember_UserId(Long userId);
    Optional<Scrap> findByBoard_BoardIdAndMember_UserId(Long boardId, Long userId);

    @Query("SELECT COUNT(g) FROM Good g WHERE g.objectId = :boardId AND g.type = 'BOARD'")
    Long countGoodsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(r) FROM Reply r WHERE r.board.boardId = :boardId")
    Long countRepliesByBoardId(@Param("boardId") Long boardId);

}
