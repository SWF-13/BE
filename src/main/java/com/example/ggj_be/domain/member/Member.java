package com.example.ggj_be.domain.member;


import com.example.ggj_be.domain.board.Board;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.ggj_be.domain.board.Board;
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
    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Board> boards;

    //비밀번호 변경 시 이용
    public void changePassword(String newPassword) {
        if(newPassword.length() <8){
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        this.password = newPassword;
    }

    //닉네임 변경
    public void changeNickName(String newNickName) {
        if(newNickName.length() >8){
            throw new IllegalArgumentException("닉네임은 8자를 초과 할 수 없습니다.");
        }
        this.nickName = newNickName;
    }

    //프로필 사진 변경
    public void updateProfileImage(String imageUrl) {
        this.userImg = imageUrl;  // 프로필 이미지 필드에 새 URL 저장
    }

    }
