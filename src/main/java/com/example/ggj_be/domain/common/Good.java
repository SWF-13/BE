package com.example.ggj_be.domain.common;


import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Long good_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(nullable = false)
    private long object_id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private LocalDateTime created_at;





    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.created_at = LocalDateTime.now();
    }

}
