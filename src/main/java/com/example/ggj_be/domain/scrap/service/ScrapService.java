package com.example.ggj_be.domain.scrap.service;

import com.example.ggj_be.domain.scrap.dto.ScrapChangeRequest;


public interface ScrapService {
    Boolean scrapChange(Long userId, ScrapChangeRequest request);

}
