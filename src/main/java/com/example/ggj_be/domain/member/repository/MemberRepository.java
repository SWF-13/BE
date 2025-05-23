package com.example.ggj_be.domain.member.repository;

import com.example.ggj_be.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccountid(String accountid);

    boolean existsByAccountid(String accountid);

    boolean existsByNickName(String nickName);

    Optional<Member> findByEmail(String email);

}
