package com.example.ggj_be.domain.board;
import com.example.ggj_be.domain.common.Good;
import com.example.ggj_be.domain.member.Member;

import com.example.ggj_be.domain.reply.Reply;
import com.example.ggj_be.domain.scrap.Scrap;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JsonBackReference
    private Member member;
    
    @Column(nullable = true, length = 100)
    @Size(max = 30, message = "제목은 30자를 초과할 수 없습니다.")
    private String title;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(max = 500, message = "내용은 500자를 초과할 수 없습니다.")
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Good> goods;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> replies;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps;



    // setter 메서드 추가
    public void setAccAt(LocalDateTime accAt) {
        this.accAt = accAt;
    }

    // getter 메서드
    public LocalDateTime getAccAt() {
        return accAt;
    }

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