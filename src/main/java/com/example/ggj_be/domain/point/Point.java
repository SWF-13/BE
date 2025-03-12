package com.example.ggj_be.domain.point;

import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;
    
    @Column(nullable = false)
    private Long changePoint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointType pointType;

    @Column(nullable = true)
    @Lob
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime accAt;

    //생성시 자동 now()설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void setAccAt() {
        this.accAt = LocalDateTime.now();
    }


    public void setPointComment(String comment) {
        this.comment = comment;
    }

     //멤버 탈퇴시 사용
     public void unlinkMember() {
        this.member = null;
    }

}
