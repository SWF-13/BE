package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.scrap.Scrap;
import com.example.ggj_be.domain.scrap.dto.ScrapChangeRequest;
import com.example.ggj_be.domain.scrap.repository.ScrapRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScrapServiceImpl implements ScrapService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ScrapRepository scrapRepository;


    @Override
    public Boolean scrapChange(Long userId, ScrapChangeRequest request) {
        try{
            Member member = memberRepository.findById
                            (userId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            Board board = boardRepository.findById
                            (request.getBoardId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));

            if (request.getScrapChk() == 0) {
                Scrap scrap = Scrap.builder()
                        .board(board)
                        .member(member)
                        .build();

                scrapRepository.save(scrap);
            } else {
                scrapRepository.deleteByMember_UserIdAndBoard_BoardId(request.getUserId(), request.getBoardId());
            }


            return true;
        }catch (Exception e){
            log.error("Error change good", e);
            return false;
        }
    }
    
}
