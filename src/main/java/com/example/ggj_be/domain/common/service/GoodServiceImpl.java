package com.example.ggj_be.domain.common.service;

import com.example.ggj_be.domain.common.Good;
import com.example.ggj_be.domain.enums.Type;
import com.example.ggj_be.domain.common.dto.GoodChangeRequest;
import com.example.ggj_be.domain.common.repository.GoodRepository;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;





@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GoodServiceImpl implements GoodService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GoodRepository goodRepository;


    @Override
    public Boolean goodChange(GoodChangeRequest request) {
        try{
            Member member = memberRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            if (request.getGoodChk() == 0) {
                Good good = Good.builder()
                        .objectId(request.getObjectId())
                        .member(member)
                        .type(Type.valueOf(request.getType()))
                        .build();

                goodRepository.save(good);
            } else {
                goodRepository.deleteByObjectIdAndType(request.getObjectId(), Type.valueOf(request.getType()));
            }


            return true;
        }catch (Exception e){
            log.error("Error change good", e);
            return false;
        }
    }
    
}
