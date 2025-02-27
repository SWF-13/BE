package com.example.ggj_be.domain.common.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodChangeRequest {
    private Long userId;        //나중에 삭제해야함
    private Long objectId;
    private String type;
    private int goodChk;
}
