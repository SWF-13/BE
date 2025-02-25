package com.example.ggj_be.domain.reply.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyCreateRequest {
    private Long userId;        //나중에 삭제해야함
    private Long boardId;
    private String content;
}
