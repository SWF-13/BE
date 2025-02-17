package com.example.ggj_be.domain.board;


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
    private Long board_id;

    @Column(nullable = false)
    private long category_id;

    @Column(nullable = false)
    private long user_seq;
    
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

}
