package com.example.ggj_be.domain.point.dto;


import com.example.ggj_be.domain.enums.Bank;
import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.point.Point;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PointUser {
    private Long pointId;
    private Long changePoint;
    private PointType pointType;
    private String comment;
    private LocalDateTime createdAt;
    private String accountid;  // Member 정보 추가
    private Bank bank;
    private String bankAccount;
    private Long point;

    @Builder
    public PointUser(Point point) {
        this.pointId = point.getPointId();
        this.changePoint = point.getChangePoint();
        this.comment = point.getComment();
        this.pointType = point.getPointType();
        this.createdAt = point.getCreatedAt();
        this.accountid = point.getMember().getAccountid();
        this.bank = point.getMember().getBankName();
        this.bankAccount = point.getMember().getBankAccount();
        this.point = point.getMember().getPoint();
    }
}
