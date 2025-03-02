package com.example.ggj_be.domain.board.controller;

import com.example.ggj_be.domain.board.dto.*;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.common.repository.PotoRepository;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.dto.ReplyDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.ggj_be.domain.board.service.BoardService;
import com.example.ggj_be.domain.reply.service.ReplyService;
import com.example.ggj_be.global.annotation.AuthMember;
import com.example.ggj_be.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.core.io.Resource;
import java.net.URLEncoder;


@Slf4j
@Tag(name = "게시판 관련 API 명세서", description = "게시판 작성, 조회, 수정, 삭제 처리하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")

public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private static final String UPLOAD_DIR = Paths.get("").toAbsolutePath().toString()+File.separator+"src"+File.separator+"uploads"+File.separator; // 저장할 디렉토리

    @Autowired
    private PotoRepository potoRepository;


    @Operation(summary = "게시글 작성 API", description = "게시글 작성 시 사진도 함께 업로드 됩니다.")
    @PostMapping
    @Transactional
    public ApiResponse<Boolean> createBoard(@AuthMember Member member,
                                            @ModelAttribute BoardCreateRequest request,
                                            @RequestParam(value = "board_files", required = false) List<MultipartFile> boardFiles) {

        Long userId = member.getUserId();

        try {
            List<String> savedFilePaths = new ArrayList<>();
            Long result = boardService.createBoard(userId, request);

            if (result == 0L){
                return ApiResponse.onFailure("403", "포인트가 부족합니다.", false);
            }else{
                if (boardFiles != null && !boardFiles.isEmpty()) {

                    for (MultipartFile file : boardFiles) {
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
                                    .type(Type.board)
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

                            return ApiResponse.onFailure("게시판 이미지 저장 실패!","게시판 이미지 저장 실패!", false);
                        }

                    }
                }
            }

        } catch (Exception e) {
            return ApiResponse.onFailure("게시판 작성 실패!","게시판 작성 실패!", false);
        }


        return ApiResponse.onSuccess(true);
    }



    @GetMapping("/home_list")
    public ApiResponse<BoardHomeListResponse> getBoardHomeListResponse(@AuthMember Member member,
                                                                                  @RequestParam(value = "listType") int listType) {


        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }


        List<BoardHomeList> homeList = boardService.getBoardHomeList(userId, listType);

        if (listType == 5) {
            return ApiResponse.onSuccess(new BoardHomeListResponse(homeList, null));
        }

        List<BoardHomeList> competitionList = boardService.getBoardHomeList(userId, 4);

        BoardHomeListResponse response = new BoardHomeListResponse(homeList, competitionList);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/searchBoardList")
    public ApiResponse<List<BoardHomeList>> getSearchBoardListResponse(@AuthMember Member member,
                                                                       @RequestParam(value = "search") String search) {

        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }

        List<BoardHomeList> response = boardService.getSearchBoardList(userId, search);


        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/categoryBoardList")
    public ApiResponse<List<BoardHomeList>> getCategoryBoardListResponse(@AuthMember Member member,
                                                                       @RequestParam(value = "categoryId") int categoryId) {


        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }

        List<BoardHomeList> response = boardService.getCategoryBoardList(userId, categoryId);


        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/detail")
    public ApiResponse<BoardDetailResponse> getBoardDetailResponse(@AuthMember Member member,
                                                                    @RequestParam(value = "boardId") Long boardId) {

        Long userId = null;

        if (member != null) {
            userId = member.getUserId();
        }

        BoardDetail boardDetail = boardService.getBoardDetail(userId, boardId);
        List<Poto> boardImages = boardService.getImages(Type.board, boardId);

        List<ReplyDetailResponse> replyList = replyService.getReplyList(userId, boardId);

        BoardDetailResponse response = new BoardDetailResponse(boardDetail, boardImages, replyList);

        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping
    public ApiResponse<Boolean> boardDelete(@RequestParam(value = "boardId") Long boardId) {

        Boolean response = boardService.boardDelete(boardId);

        if (response) {
            return ApiResponse.onSuccess(true);
        } else {
            return ApiResponse.onFailure("게시글 삭제 실패", "게시글 삭제 실패", false);
        }
    }

    @PatchMapping
    public ApiResponse<Boolean> boardAccAtUdate(@AuthMember Member member,
                                                @RequestParam(value = "boardId") Long boardId,
                                                @RequestParam(value = "replyId") Long replyId) {
        Long userId = member.getUserId();
        Boolean chk = boardService.chkUser(boardId, userId);

        if (chk){
            Boolean response = boardService.boardAccAtUdate(boardId, replyId);

            if (response) {
                return ApiResponse.onSuccess(true);
            } else {
                return ApiResponse.onFailure("채택하기 실패!", "채택하기 실패!", false);
            }
        }else{
            return ApiResponse.onFailure("본인이 작성한 게시글이 아닙니다.", "본인이 작성한 게시글이 아닙니다.", false);
        }
    }


    @GetMapping("/download")
    ResponseEntity<?> imgDownload(@AuthMember Member member,
                                  @RequestParam(value = "boardId") Long boardId,
                                  @RequestParam(value = "replyId") Long replyId) {

        Long userId = member.getUserId();
        Boolean chk = boardService.chkUser(boardId, userId);

        if (!chk) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("code", "FORBIDDEN");
            errorDetails.put("message", "본인이 작성한 게시글이 아닙니다.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorDetails);
        }

        try {
            List<Poto> imageNames = boardService.getImageName(replyId, Type.reply);
            File tempZipFile = File.createTempFile("images", ".zip");
            try (FileOutputStream fos = new FileOutputStream(tempZipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (Poto imageName : imageNames) {
                    log.info("imageName : {}", imageName.getPotoName());
                    File imageFile = new File(UPLOAD_DIR + imageName.getPotoName());
                    if (imageFile.exists()) {
                        try (FileInputStream fis = new FileInputStream(imageFile)) {
                            ZipEntry zipEntry = new ZipEntry(imageName.getPotoOrigin());
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, length);
                            }
                            zos.closeEntry();
                        }
                    }
                }
            }

            FileSystemResource resource = new FileSystemResource(tempZipFile);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=images.zip")
                    .body(resource);

        } catch (IOException e) {
            log.error("Error occurred while processing the download: {}", e.getMessage());

            Map<String, String> errorDetails = new HashMap<>();

            errorDetails.put("code", "INTERNAL_SERVER_ERROR");
            errorDetails.put("message", "이미지 압축에 실패하였습니다.");


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorDetails);
        }
    }
}