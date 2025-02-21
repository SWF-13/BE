package com.example.ggj_be.domain.re_reply;


import jakarta.persistence.*;
import lombok.*;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.Reply;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replyId", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Reply reply;
    
    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = true)
    private LocalDateTime accAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;


    

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

}
