package com.example.ggj_be.domain.board;


import com.example.ggj_be.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    @Column(name = "board_id")
    private Long  board_id;

    @Column(nullable = false)
    private Long category_id;

//    @Column(nullable = false)
//    private long user_seq;

    @Column(nullable = true, length = 100)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    @Column(nullable = true)
    private int board_prize;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    private LocalDateTime end_at;

    @Column(nullable = true)
    private LocalDateTime acc_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = true, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;


    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    ////수정정시 자동 now()설정
    @PreUpdate
    protected void onUpdate() {
    this.updated_at = LocalDateTime.now();
    }

    //멤버 탈퇴시 사용
    public void unlinkMember() {
        this.member = null;
    }


}
