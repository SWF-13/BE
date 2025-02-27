package com.example.ggj_be.domain.common.controller;

import com.example.ggj_be.domain.common.service.CategoryService;
import com.example.ggj_be.domain.common.Category;
import com.example.ggj_be.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "카테고리 관련 API 명세서", description = "카테고리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<Category>>getCategoryListResponse() {

        List<Category> result = categoryService.getCategoryList();
        return ApiResponse.onSuccess(result);
    }
}
