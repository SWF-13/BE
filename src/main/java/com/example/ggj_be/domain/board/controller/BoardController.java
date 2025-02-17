package com.example.ggj_be.domain.board.controller;

import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.common.repository.PotoRepository;
import com.example.ggj_be.domain.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.ggj_be.domain.board.service.BoardService;
import com.example.ggj_be.domain.board.dto.BoardCreateRequest;
import com.example.ggj_be.domain.board.dto.BoardSelectEndRequest;


import com.example.ggj_be.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.util.UUID;



@Slf4j
@Tag(name = "게시판 관련 API 명세서", description = "게시판 작성, 조회, 수정, 삭제 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

    private final BoardService boardService;
    private static final String UPLOAD_DIR = Paths.get("").toAbsolutePath().toString()+File.separator+"src"+File.separator+"uploads"+File.separator; // 저장할 디렉토리

    @Autowired
    private PotoRepository potoRepository;


    @Operation(summary = "게시글 작성 API", description = "게시글 작성 시 사진도 함께 업로드 됩니다.")
    @PostMapping
    @Transactional
    public ApiResponse<Boolean> createBoard(@ModelAttribute BoardCreateRequest request,
                                            @RequestParam(value = "board_files", required = false) List<MultipartFile> boardFiles) {
        try {
            List<String> savedFilePaths = new ArrayList<>();
            Long result = boardService.createBoard(request);

            if (boardFiles != null && !boardFiles.isEmpty()) {

                for (MultipartFile file : boardFiles) {
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // 중복 방지
                    Path filePath = Paths.get(UPLOAD_DIR + fileName);

                    log.info("파일 유유이름: " + fileName);
                    // 파일 저장 로직 구현
                    try {
                        File dir = new File(UPLOAD_DIR);
                        if (!dir.exists()) dir.mkdirs(); // 디렉토리가 없으면 생성
                        file.transferTo(filePath.toFile()); // 파일 저장
                        savedFilePaths.add(filePath.toString());

                        Poto poto = Poto.builder()
                                .object_id(result)
                                .type(Type.board)
                                .poto_name(fileName)
                                .poto_origin(file.getOriginalFilename())
                                .build();

                        potoRepository.save(poto);

                    } catch (IOException e) {
                        try {
                            Files.delete(filePath);
                        } catch (IOException deleteException) {
                            log.error("파일 삭제 실패: {}", filePath, deleteException);
                        }

                        throw new RuntimeException("게시판 작성 실패!", e);
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return ApiResponse.onSuccess(true);
    }

    @GetMapping("/hello")
    public String test() {
        return "Hello, world!";
    }

    @GetMapping("/end_count")
    public ApiResponse<List<BoardSelectEndRequest>> getBoardSelectEndRequests() {
        List<BoardSelectEndRequest> result = boardService.getBoardSelectEndRequests();
        return ApiResponse.onSuccess(result);
    }
}