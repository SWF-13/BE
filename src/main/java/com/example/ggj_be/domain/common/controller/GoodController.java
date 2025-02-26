package com.example.ggj_be.domain.common.controller;

import com.example.ggj_be.domain.common.dto.GoodChangeRequest;
import com.example.ggj_be.domain.common.service.GoodService;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "좋아요 관련 API 명세서", description = "좋아요 추가, 삭제 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/good")
public class GoodController {
    private final GoodService goodService;
    @PostMapping
    @Transactional
    
    public ApiResponse<Boolean>  goodChange(@AuthMember Member member,
                                            @RequestBody GoodChangeRequest request) {

        Long userId = member.getUserId();
        Boolean result = goodService.goodChange(userId, request);
        return ApiResponse.onSuccess(result);
    }
}
