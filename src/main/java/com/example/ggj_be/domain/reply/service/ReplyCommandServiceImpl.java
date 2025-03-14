package com.example.ggj_be.domain.reply.service;

import com.example.ggj_be.domain.board.repository.BoardRepository;
import com.example.ggj_be.domain.common.repository.GoodRepository;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.reply.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.reply.repository.ReplyRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyCommandServiceImpl implements ReplyCommandService {
    private final ReplyRepository replyRepository;
    private final GoodRepository goodRepository;
    private final BoardRepository boardRepository;

    @Override
    public List<MyPageCommentResponse> getMyComments(Member member) {
        Long userId = member.getUserId();
        List<Reply> replies = replyRepository.findByMember_UserId(userId);

        return replies.stream()
                .map(reply -> {
                    int goodsCount = Math.toIntExact(boardRepository.countGoodsByBoardId(reply.getBoard().getBoardId()));
                    int goodChk = goodRepository.existsByMember_UserIdAndObjectIdAndType(userId, reply.getReplyId(), Type.reply) ? 1 : 0;
                    int replyCount = boardRepository.countRepliesAndReRepliesByBoardId(reply.getBoard().getBoardId()).intValue();
                    return new MyPageCommentResponse(reply, goodsCount, goodChk, replyCount);
                })
                .sorted(Comparator.comparing(MyPageCommentResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
