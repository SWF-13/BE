package com.example.ggj_be.domain.board.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import com.example.ggj_be.domain.common.Poto;

@Getter
@AllArgsConstructor
public class BoardDetailResponse {
    private BoardDetail boardDetail;
    private List<Poto> boardImages;  // 게시글 사진
    private List<ReplyDetailResponse> replyList;  // 댓글,대댓글,댓글사진
}
