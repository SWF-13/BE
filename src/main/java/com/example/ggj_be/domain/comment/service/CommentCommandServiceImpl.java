package com.example.ggj_be.domain.comment.service;

import com.example.ggj_be.domain.comment.Comment;
import com.example.ggj_be.domain.comment.dto.MyPageCommentResponse;
import com.example.ggj_be.domain.comment.repository.CommentRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.Reply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService{
    private final CommentRepository commentRepository;

    @Override
    public List<MyPageCommentResponse> getMyComments(Member member) {
        Long userId = member.getUserSeq();
        List<Reply> replies = commentRepository.findByMember_UserSeq(userId);
        return replies.stream()
                .map(MyPageCommentResponse::new)
                .collect(Collectors.toList());
    }
}
