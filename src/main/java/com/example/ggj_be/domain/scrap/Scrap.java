package com.example.ggj_be.domain.scrap;


import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scrap_tb")
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id; //스크랩 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private Member member;  // 회원 정보 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;  // 게시판 정보 (외래키)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 댓글 생성 시간
}
