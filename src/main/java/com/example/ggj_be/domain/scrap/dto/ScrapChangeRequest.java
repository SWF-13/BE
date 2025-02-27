package com.example.ggj_be.domain.scrap.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapChangeRequest {
    private Long userId;        //나중에 삭제해야함
    private Long boardId;
    private int scrapChk;
}
