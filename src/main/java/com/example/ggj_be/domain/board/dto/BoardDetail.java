package com.example.ggj_be.domain.board.dto;

import java.time.LocalDateTime;

public interface BoardDetail {
    Long getBoardId();              //보드아이디
    String getNickName();           //게시글 닉네임
    String getUserImg();       //사용자이미지
    int getEndCount();              //디데이 카운트
    String getTitle();              //제목
    String getCategoryName();       //카테고리이름
    String getCreatedElapsed();    //게시글작성시간시간과 현재시간비교
    Long getBoardPrize();           //상금
    String getContent();            //내용
    int getIsWriter();              //내가 작성자인지
    int getGoodCount();             //좋아요수
    int getReplyCount();            //댓글,대댓글갯수
    int getGoodChk();               //내가누른 좋아요인지 확인 0:안누름 1:누름
    int getScrapCount();            //스크랩갯수
    int getScrapChk();              //내가누른 스크랩인지 확인 0:안누름 1:누름


     //보드아이디, 사용자 닉네임, 사용자이미지, 디데이, 타이틀, 카테고리, 게시글만든시간, 상금, 내용, 내가 작성자인지, 좋아요갯수, 댓글갯수, 내가누른 좋아요인지, 스크랩갯수, 내가누른 스크랩인지 확인
}
