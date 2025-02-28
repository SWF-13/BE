package com.example.ggj_be.domain.point.dto;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.point.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class pointUserListResponse {
    private List<Point> addPointUserList;
    private List<Point> removePointUserList;
    private List<Member> UserAllList;
}
