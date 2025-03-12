package com.example.ggj_be.domain.common;


import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.reply.Reply;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "good")
public class Good {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long goodId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reply reply;

    @Column(nullable = false)
    private long objectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private LocalDateTime createdAt;





    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
