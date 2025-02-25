package com.example.ggj_be.domain.scrap.repository;

import com.example.ggj_be.domain.scrap.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ScrapRepository extends JpaRepository<Scrap, Long>{
    void deleteByMember_UserIdAndBoard_BoardId(Long userId, Long boardId);
    List<Scrap> findByMember_UserId(Long userId);
}
