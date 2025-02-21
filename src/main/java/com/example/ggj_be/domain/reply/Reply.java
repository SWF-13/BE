package com.example.ggj_be.domain.reply;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.re_reply.Re_reply;
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
    private Long reply_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board;
    
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
