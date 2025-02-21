package com.example.ggj_be.domain.board;
import com.example.ggj_be.domain.member.Member;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long boardId;

    @Column(nullable = false)
    private long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
    
    @Column(nullable = true, length = 100)
    private String title;
    
    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = true)
    private Long boardPrize;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = true)
    private LocalDateTime accAt;
    

    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    ////수정정시 자동 now()설정
    @PreUpdate
    protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
    }

    //멤버 탈퇴시 사용
    public void unlinkMember() {
        this.member = null;
    }

}
