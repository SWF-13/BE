package com.example.ggj_be.domain.point.controller;

import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.point.Point;
import com.example.ggj_be.domain.point.dto.CommentRequest;
import com.example.ggj_be.domain.point.dto.PointCreateRequest;
import com.example.ggj_be.domain.point.dto.PointUpateRequest;
import com.example.ggj_be.domain.point.dto.pointUserListResponse;
import com.example.ggj_be.domain.point.service.PointService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "포인트 관련 API 명세서", description = "포인트 관련 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {
    private final PointService pointService;


    @GetMapping("/userList")
    @Transactional

    public ApiResponse<pointUserListResponse>getUserList(@AuthMember Member member) {

        // 관리자 권한이 아닐 경우 요청 차단
        if (member.getRole() != Role.ADMIN) {
            return ApiResponse.onFailure("403", "관리자만 접근할 수 있습니다.", null);
        }

        List<Point> addUserList = pointService.getUserList(PointType.add);
        List<Point> removeUserList = pointService.getUserList(PointType.remove);
        List<Member> userAllList = pointService.getAllUserList();

        pointUserListResponse response = new pointUserListResponse(addUserList, removeUserList, userAllList);

        return ApiResponse.onSuccess(response);
    }

    @PostMapping
    @Transactional
    public ApiResponse<Boolean>createPointRequest(@AuthMember Member member,
                                                  @ModelAttribute PointCreateRequest request) {


        Boolean result = pointService.createPoint(member, request);

        if(!result) {
            return ApiResponse.onFailure("403", "포인트가 부족합니다.", false);
        }

        return ApiResponse.onSuccess(true);
    }

    @PostMapping("/updatePoint")
    @Transactional
    public ApiResponse<Boolean>updatePointRequest(@AuthMember Member member,
                                                  @ModelAttribute PointUpateRequest request) {

        // 관리자 권한이 아닐 경우 요청 차단
        if (member.getRole() != Role.ADMIN) {
            return ApiResponse.onFailure("403", "관리자만 접근할 수 있습니다.", null);
        }

        Boolean result = pointService.updatePoint(request);

        if(!result) {
            return ApiResponse.onFailure("403", "포인트 수정에 실패하였습니다.", false);
        }

        return ApiResponse.onSuccess(true);
    }

    @PostMapping("/updateComment")
    @Transactional
    public ApiResponse<Boolean>updateCommentRequest(@AuthMember Member member,
                                                  @ModelAttribute CommentRequest request) {


        // 관리자 권한이 아닐 경우 요청 차단
        if (member.getRole() != Role.ADMIN) {
            return ApiResponse.onFailure("403", "관리자만 접근할 수 있습니다.", null);
        }

        Boolean result = pointService.updateComment(request);

        if(!result) {
            return ApiResponse.onFailure("403", "비고 수정에 실패하였습니다.", false);
        }

        return ApiResponse.onSuccess(true);
    }
}
