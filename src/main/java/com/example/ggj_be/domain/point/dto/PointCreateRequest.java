package com.example.ggj_be.domain.point.dto;

import com.example.ggj_be.domain.enums.PointType;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class PointCreateRequest {
    private Long changePoint;
    private PointType pointType;

}