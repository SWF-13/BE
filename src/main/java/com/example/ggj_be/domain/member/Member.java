package com.example.ggj_be.domain.member;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.*;
import com.example.ggj_be.domain.enums.Role;

import java.time.LocalDate;

@Entity
@Builder
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
    private Long id;  // 고유 회원번호

    @Column(name = "account_id")
    private String  accountid;//이메일 형식으로 사용

    @Column(nullable = false, length = 20)
    private String nameKo;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String memberNo;

    @Column(nullable = false)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate joinDt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "user_img", nullable = true, length = 255)
    private String userImg;  // 프로필 이미지 경로

    @Column(name = "user_birth", nullable = false)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate userBirth;

    @Column(name = "agree_service", nullable = false)
    private Boolean agreeService;  // 서비스 이용 약관 동의 여부

    @Column(name = "agree_info", nullable = false)
    private Boolean agreeInfo;  // 개인정보 처리방침 동의 여부

    //비밀번호 변경 시 이용
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
