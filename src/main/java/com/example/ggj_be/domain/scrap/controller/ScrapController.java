package com.example.ggj_be.domain.scrap.controller;

import com.example.ggj_be.domain.scrap.dto.ScrapChangeRequest;
import com.example.ggj_be.domain.scrap.service.ScrapService;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "스크랩 관련 API 명세서", description = "스크랩 추가, 삭제 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scrap")
public class ScrapController {
    private final ScrapService scrapService;
    @PostMapping
    @Transactional
                                            //@AuthMember Member member 변경해야함!!!!!!!!!!!!!!!!!!!!!!!!!
    public ApiResponse<Boolean>  scrapChange(@RequestBody ScrapChangeRequest request) {

        log.info("파람확인 {}, {}, {}", request.getUserId(), request.getBoardId(), request.getScrapChk());
        Boolean result = scrapService.scrapChange(request);
        return ApiResponse.onSuccess(result);
    }
}
