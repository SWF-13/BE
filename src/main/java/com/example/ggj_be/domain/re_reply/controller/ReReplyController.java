package com.example.ggj_be.domain.re_reply.controller;

import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.re_reply.dto.ReReplyCreateRequest;
import com.example.ggj_be.domain.re_reply.service.ReReplyService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "대댓글 관련 API 명세서", description = "대댓글관련 처리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reReply")
public class ReReplyController {
    private final ReReplyService reReplyService;


    @PostMapping
    @Transactional
    public ApiResponse<Boolean>  replyCreate(@AuthMember Member member,
                                             @ModelAttribute ReReplyCreateRequest request) {

        Long userId = member.getUserId();
        Boolean result = reReplyService.createReReply(userId, request);

        return ApiResponse.onSuccess(result);
    }

}
