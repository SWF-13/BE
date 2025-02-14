package com.example.ggj_be.domain.comment.repository;

import com.example.ggj_be.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMember_UserId(Long userId);
}
