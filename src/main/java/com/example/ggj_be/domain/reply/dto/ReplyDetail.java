package com.example.ggj_be.domain.reply.dto;


public interface ReplyDetail {
    Long getReplyId();
    int getAccChk();
    String getContent();
    String getNickName();
    String getUserImg();
    int getIsWriter();
    int getGoodChk();
    int getGoodCount();

}