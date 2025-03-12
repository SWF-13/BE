package com.example.ggj_be.domain.point.service;

import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.point.Point;
import com.example.ggj_be.domain.point.dto.CommentRequest;
import com.example.ggj_be.domain.point.dto.PointCreateRequest;
import com.example.ggj_be.domain.point.dto.PointUpateRequest;

import java.util.List;

public interface PointService {
    List<Point> getUserList(PointType pointType);
    List<Member> getAllUserList();
    Boolean createPoint(Member member, PointCreateRequest request);
    Boolean updatePoint(PointUpateRequest request);
    Boolean updateComment(CommentRequest request);
    List<Point>getPointList(Member member, String period);

}
