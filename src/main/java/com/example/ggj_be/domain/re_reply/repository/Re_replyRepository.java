package com.example.ggj_be.domain.re_reply.repository;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.re_reply.Re_reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Re_replyRepository extends JpaRepository<Re_reply, Long> {
    List<Re_reply> findByMember(Member member);
}
