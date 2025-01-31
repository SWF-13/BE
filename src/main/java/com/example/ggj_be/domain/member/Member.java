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
@Table(name = "employee_tb",
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueAccountId", columnNames = {"account_id"})
        }
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private String  accountid;

    @Column(nullable = false, length = 20)
    private String nameKo;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String employeeNo;

    @Column(nullable = false)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate joinDt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    //비밀번호 변경 시 이용
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
