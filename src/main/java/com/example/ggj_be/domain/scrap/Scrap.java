package com.example.ggj_be.domain.scrap;

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
    private Long scrap_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;

    @Column(nullable = false)
    private LocalDateTime created_at;





    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

}
