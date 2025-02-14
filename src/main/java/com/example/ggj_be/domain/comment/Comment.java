package com.example.ggj_be.domain.comment;


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
@Table(name = "comment_tb")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;  // 댓글 고유번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private Member member;  // 회원 정보 (외래키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", nullable = false)
    private Board board;  // 게시판 정보 (외래키)

    @Column(name = "reply_content", nullable = false, length = 255)
    private String content;  // 댓글 내용

    @Column(name = "acc_at")
    private LocalDateTime accAt;  // 채택 시간 (NULL 허용)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 댓글 생성 시간

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;  // 댓글 수정 시간

    // 댓글 수정 메서드
    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
}