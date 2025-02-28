package com.example.ggj_be.domain.point.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentRequest {

    private Long userId;
    private Long pointId;
    private String listType;
    private String comment;

}