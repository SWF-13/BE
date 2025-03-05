package com.example.ggj_be.domain.scrap.repository;
import com.example.ggj_be.domain.scrap.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ScrapRepository extends JpaRepository<Scrap, Long>{
    List<Scrap> findByMember_UserId(Long userId);
    Optional<Scrap> findByBoardIdAndUserId(Long boardId, Long userId);
}
