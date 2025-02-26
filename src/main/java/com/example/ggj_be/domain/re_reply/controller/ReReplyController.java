package com.example.ggj_be.domain.re_reply.controller;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.re_reply.dto.ReReplyCreateRequest;
import com.example.ggj_be.domain.re_reply.service.ReReplyService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Tag(name = "대댓글 관련 API 명세서", description = "대댓글관련 처리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reReply")
public class ReReplyController {
    private final ReReplyService reReplyService;


    @PostMapping
    @Transactional
                                            //@AuthMember Member member 변경해야함!!!!!!!!!!!!!!!!!!!!!!!!!
    public ApiResponse<Boolean>  replyCreate(@AuthMember Member member,
                                             @ModelAttribute ReReplyCreateRequest request) {
        
        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }             
        Boolean result = reReplyService.createReReply(userId, request);

        return ApiResponse.onSuccess(result);
    }

}
