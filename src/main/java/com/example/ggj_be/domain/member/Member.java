package com.example.ggj_be.domain.member;


import com.example.ggj_be.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.*;
import com.example.ggj_be.domain.enums.Role;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_tb",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueAccountId", columnNames = {"account_id"})
        }
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;  // 고유 회원번호

    @Column(name = "account_id")
    private String  accountid;
    @Column(nullable = false, length = 20)
    private String nameKo;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickName;

    @Column(nullable = false, length = 100)
    private String email;

//    @Column(nullable = false, length = 20)
//    private String memberNo;//전화번호

    @Column(nullable = false)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate joinDt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "user_img", nullable = true, length = 255)
    private String userImg;  // 프로필 이미지 경로

    @Column(name = "user_birth", nullable = true)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate userBirth;

    @Column(name = "agree_service", nullable = false)
    private Boolean agreeService;  // 서비스 이용 약관 동의 여부

    @Column(name = "agree_info", nullable = false)
    private Boolean agreeInfo;  // 개인정보 처리방침 동의 여부

    @Column(name = "bank_account", nullable = true, length = 20)
    private String bankAccount;

    @Column(name = "bank_name", nullable = true, length = 20)
    private String bankName;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    //비밀번호 변경 시 이용
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

//    public Member toBuilder() {
//        return Member.builder()
//                .userId(this.userId)
//                .accountid(this.accountid)
//                .nameKo(this.nameKo)
//                .password(this.password)
//                .nickName(this.nickName)
//                .email(this.email)
//                .joinDt(this.joinDt)
//                .role(this.role)
//                .userImg(this.userImg)
//                .userBirth(this.userBirth)
//                .agreeService(this.agreeService)
//                .agreeInfo(this.agreeInfo)
//                .bankAccount(this.bankAccount)
//                .bankName(this.bankName)
//                .boards(this.boards)
//                .build();
//    }
}
