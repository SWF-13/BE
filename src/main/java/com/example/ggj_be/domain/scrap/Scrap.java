package com.example.ggj_be.domain.scrap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.board.Board;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scrap")
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long scrapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonBackReference
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonBackReference
    private Board board;

    @Column(nullable = false)
    private LocalDateTime createdAt;





    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
