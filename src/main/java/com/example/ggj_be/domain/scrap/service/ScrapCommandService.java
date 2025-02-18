package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.scrap.dto.ScrapDto;

import java.util.List;

public interface ScrapCommandService {
    List<ScrapDto> getScraps(Member member);
}
