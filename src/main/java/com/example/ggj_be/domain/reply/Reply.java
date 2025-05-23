package com.example.ggj_be.domain.reply;


import com.example.ggj_be.domain.common.Good;
import com.example.ggj_be.domain.re_reply.Re_reply;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.board.Board;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "userId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "boardId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Board board;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Good> goods;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Re_reply> re_replies;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = true)
    private LocalDateTime accAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


//    @OneToMany(mappedBy = "reply", fetch = FetchType.LAZY)
//    private List<Re_reply> reReplies;  // 대댓글 목록

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