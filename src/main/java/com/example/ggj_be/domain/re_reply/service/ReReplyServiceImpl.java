package com.example.ggj_be.domain.re_reply.service;

import com.example.ggj_be.domain.re_reply.Re_reply;
import com.example.ggj_be.domain.re_reply.repository.Re_replyRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.reply.Reply;
import com.example.ggj_be.domain.re_reply.dto.ReReplyCreateRequest;
import com.example.ggj_be.domain.reply.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReReplyServiceImpl implements ReReplyService {

    @Autowired
    private Re_replyRepository reReplyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReplyRepository replyRepository;


    @Override
    public Boolean createReReply(ReReplyCreateRequest request) {

        try{
            Member member = memberRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Reply reply = replyRepository.findById(request.getReplyId())
                    .orElseThrow(() -> new RuntimeException("Board not found"));
            Re_reply reReply = Re_reply.builder()
                    .member(member)
                    .reply(reply)
                    .content(request.getContent())
                    .build();

            reReplyRepository.save(reReply);
            return true;
        }catch (Exception e){
            log.error(" Error creating board", e);
            throw new RuntimeException("게시글 생성 실패", e);
        }
    }
    
}
