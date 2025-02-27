package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.board.dto.MyPageBoardResponse;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.scrap.Scrap;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;
import com.example.ggj_be.domain.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class ScrapCommandServiceImpl implements ScrapCommandService {
    private final ScrapRepository scrapRepository;

    @Override
    public List<ScrapDto> getScraps(Member member) {
        Long userId = member.getUserId();
        List<Scrap> scraps = scrapRepository.findByMember_UserId(userId);
        return scraps.stream()
                .map(ScrapDto::new)
                .sorted(Comparator.comparing(ScrapDto::getScrap_createdAt).reversed())
                .collect(Collectors.toList());
    }
}
