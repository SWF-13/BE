package com.example.ggj_be.domain.point.dto;

import com.example.ggj_be.domain.enums.PointType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PointUpateRequest {
    private Long userId;
    private Long pointId;
    private String listType;
    private PointType pointType;
    private Long changePoint;

}