package com.example.ggj_be.domain.re_reply;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "re_reply")
public class Re_reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long re_reply_id;

    @Column(nullable = false)
    private long user_seq;

    @Column(nullable = false)
    private long reply_id;
    
    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = true)
    private LocalDateTime acc_at;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime updated_at;


    

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
