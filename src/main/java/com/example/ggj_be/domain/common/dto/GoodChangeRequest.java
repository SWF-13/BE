package com.example.ggj_be.domain.common.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodChangeRequest {
    private Long objectId;
    private String type;
}
