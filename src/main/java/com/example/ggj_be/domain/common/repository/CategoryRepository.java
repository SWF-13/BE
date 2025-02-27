package com.example.ggj_be.domain.common.repository;

import com.example.ggj_be.domain.common.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findAll();
}
