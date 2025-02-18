package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.scrap.Scrap;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ScrapCommandServiceImpl implements ScrapCommandService {
    private final ScrapRepository scrapRepository;

    @Override
    public List<ScrapDto> getScraps(Member member) {
        Long userId = member.getUserSeq();
        List<Scrap> scraps = scrapRepository.findByMember_UserSeq(userId);
        return scraps.stream()
                .map(ScrapDto::new)
                .collect(Collectors.toList());
    }
}
