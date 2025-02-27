package com.example.ggj_be.domain.reply.service;

import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.re_reply.dto.ReReplyDetail;
import com.example.ggj_be.domain.reply.Reply;
import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.reply.dto.ReplyCreateRequest;
import com.example.ggj_be.domain.reply.dto.ReplyDetail;
import com.example.ggj_be.domain.reply.dto.ReplyDetailResponse;
import com.example.ggj_be.domain.reply.repository.ReplyRepository;
import com.example.ggj_be.domain.re_reply.repository.Re_replyRepository;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.common.repository.PotoRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PotoRepository potoRepository;

    @Autowired
    private Re_replyRepository re_replyRepository;


    @Override
    public Long createReply(Long userId, ReplyCreateRequest request) {

        try{
            Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Board board = boardRepository.findById(request.getBoardId())
                    .orElseThrow(() -> new RuntimeException("Board not found"));
            Reply reply = Reply.builder()
                    .member(member)
                    .board(board)
                    .content(request.getContent())
                    .build();

            replyRepository.save(reply);
            return reply.getReplyId();
        }catch (Exception e){
            log.error(" Error creating board", e);
            throw new RuntimeException("게시글 생성 실패", e);
        }
    }

    @Override
    public List<ReplyDetailResponse> getReplyList(Long userId, Long boardId) {
        List<ReplyDetail> replyList = replyRepository.findReplyDetail(userId, boardId);
        List<ReplyDetailResponse> replyDetailResponse = new ArrayList<>();
        for (ReplyDetail replyDetail : replyList) {
            List<ReReplyDetail> reReplyList = re_replyRepository.findReReplyDetail(userId, replyDetail.getReplyId());
            List<Poto> replyImages = potoRepository.findByTypeAndObjectId(Type.reply, replyDetail.getReplyId());
            ReplyDetailResponse getReplyDetailResponse = new ReplyDetailResponse() {
                @Override
                public Long getReplyId() {
                    return replyDetail.getReplyId();
                }

                @Override
                public int getAccChk() {
                    return replyDetail.getAccChk();
                }

                @Override
                public String getContent() {
                    return replyDetail.getContent();
                }

                @Override
                public String getNickName() {
                    return replyDetail.getNickName();
                }

                @Override
                public String getUserImg() {
                    return replyDetail.getUserImg();
                }
                @Override
                public int getIsWriter() {
                    return replyDetail.getIsWriter();
                }
                @Override
                public int getGoodChk() {
                    return replyDetail.getGoodChk();
                }
                @Override
                public int getGoodCount() {
                    return replyDetail.getGoodCount();
                }

                @Override
                public List<ReReplyDetail> getReReplyList() {
                    return reReplyList;
                }

                @Override
                public List<Poto> getReplyImages() {
                    return replyImages;
                }
            };
            replyDetailResponse.add(getReplyDetailResponse);
        }
        return replyDetailResponse;
    }
    
}
