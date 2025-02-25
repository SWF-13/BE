package com.example.ggj_be.domain.common;
import java.time.LocalDateTime;

import com.example.ggj_be.domain.enums.Type;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "poto")
public class Poto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long potoId;
    
    @Column(nullable = false)
    private long objectId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String potoName;

    @Column(nullable = false)
    private String potoOrigin;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
}
