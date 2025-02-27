package com.example.ggj_be.domain.common.service;

import com.example.ggj_be.domain.common.dto.GoodChangeRequest;


public interface GoodService {
    Boolean goodChange(Long userId, GoodChangeRequest request);

}
