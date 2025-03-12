package com.example.ggj_be.domain.reply.service;

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

    @Override
    public List<MyPageCommentResponse> getMyComments(Member member) {
        Long userId = member.getUserId();
        List<Reply> replies = replyRepository.findByMember_UserId(userId);

        return replies.stream()
                .map(reply -> {
                    int goodsCount = replyRepository.countGoodsByReplyId(reply.getReplyId()).intValue(); // 좋아요 개수 조회
                    return new MyPageCommentResponse(reply, goodsCount);
                })
                .sorted(Comparator.comparing(MyPageCommentResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }
}
