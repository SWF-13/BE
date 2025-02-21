package com.example.ggj_be.domain.board.dto;


import com.example.ggj_be.domain.common.Poto;

import java.util.List;

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