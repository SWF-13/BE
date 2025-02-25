package com.example.ggj_be.domain.common.controller;

import com.example.ggj_be.domain.common.dto.GoodChangeRequest;
import com.example.ggj_be.domain.common.service.GoodService;
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
                                            //@AuthMember Member member 변경해야함!!!!!!!!!!!!!!!!!!!!!!!!!
    public ApiResponse<Boolean>  goodChange(@RequestBody GoodChangeRequest request) {

        log.info("파람확인 {}, {}, {}, {}", request.getUserId(), request.getObjectId(), request.getType(), request.getGoodChk());
        Boolean result = goodService.goodChange(request);
        return ApiResponse.onSuccess(result);
    }
}
