package com.example.ggj_be.domain.reply.controller;

import com.example.ggj_be.domain.reply.dto.ReplyDetailResponse;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.common.repository.PotoRepository;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.dto.ReplyCreateRequest;
import com.example.ggj_be.domain.reply.service.ReplyService;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.ggj_be.global.annotation.AuthMember;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "댓글 관련 API 명세서", description = "댓글관련 처리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reply")
public class ReplyController {
    private final ReplyService replyService;
    private static final String UPLOAD_DIR = Paths.get("").toAbsolutePath().toString()+File.separator+"src"+File.separator+"uploads"+File.separator; // 저장할 디렉토리

    @Autowired
    private PotoRepository potoRepository;


    @PostMapping
    @Transactional

    public ApiResponse<Boolean>  replyCreate(@AuthMember Member member,
                                             @ModelAttribute ReplyCreateRequest request,
                                             @RequestParam(value = "reply_files", required = false) List<MultipartFile> replyFiles) {

        Long userId = member.getUserId();     

        try {
            List<String> savedFilePaths = new ArrayList<>();
            Long result = replyService.createReply(userId, request);

            if (replyFiles != null && !replyFiles.isEmpty()) {
                for (MultipartFile file : replyFiles) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // 중복 방지
                    Path filePath = Paths.get(UPLOAD_DIR + fileName);

                    // 파일 저장 로직 구현
                    try {
                        File dir = new File(UPLOAD_DIR);
                        if (!dir.exists()) dir.mkdirs(); // 디렉토리가 없으면 생성
                        file.transferTo(filePath.toFile()); // 파일 저장
                        savedFilePaths.add(filePath.toString());

                        Poto poto = Poto.builder()
                                .objectId(result)
                                .type(Type.reply)
                                .potoName(fileName)
                                .potoOrigin(file.getOriginalFilename())
                                .build();

                        potoRepository.save(poto);

                    } catch (IOException e) {
                        try {
                            Files.delete(filePath);
                        } catch (IOException deleteException) {
                            log.error("파일 삭제 실패: {}", filePath, deleteException);
                        }

                        throw new RuntimeException("공모전 출품 실패!!!", e);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ApiResponse.onSuccess(true);
    }

    @GetMapping
    public ApiResponse<List<ReplyDetailResponse>> getBoardHomeListResponse(@AuthMember Member member,
                                                                       @RequestParam(value = "boardId") Long boardId) {
        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }

        List<ReplyDetailResponse> response = replyService.getReplyList(userId, boardId);


        return ApiResponse.onSuccess(response);
    }
}
