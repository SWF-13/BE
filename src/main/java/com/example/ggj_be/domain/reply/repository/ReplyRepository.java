package com.example.ggj_be.domain.reply.repository;

import com.example.ggj_be.domain.reply.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByMember_UserId(Long userId);
}
