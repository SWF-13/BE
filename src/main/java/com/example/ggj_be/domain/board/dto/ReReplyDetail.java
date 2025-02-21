package com.example.ggj_be.domain.board.dto;

public interface ReReplyDetail {
    Long getReReplyId();            //대댓글아이디
    String getNickName();           //게시글 닉네임
    String getUserPotoName();       //사용자이미지
    String getContent();            //내용
    int getIsWriter();              //내가 작성자인지
    int getGoodChk();               //내가누른 좋아요인지 확인 0:안누름 1:누름
    int getGoodCount();             //좋아요갯수
}
