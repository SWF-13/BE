package com.example.ggj_be.domain.common;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private Long poto_id;
    
    @Column(nullable = false)
    private long object_id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private String poto_name;

    @Column(nullable = false)
    private String poto_origin;

    @Column(nullable = false)
    private LocalDateTime created_at;


    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
        this.poto_name = UUID.randomUUID().toString() + "_" + this.poto_origin;
    }
    
}
