package com.example.ggj_be.domain.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "데이터 처리 응답 DTO")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResult {

    private Long id;
    private LocalDateTime createdAt;

    public static CustomResult toCustomResult(Long id) {
        return CustomResult.builder()
                .id(id)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
