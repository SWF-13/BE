package com.example.ggj_be.domain.common.repository;

import com.example.ggj_be.domain.common.Good;
import com.example.ggj_be.domain.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GoodRepository extends JpaRepository<Good, Long>{
    void deleteByObjectIdAndType(Long objectId, Type type);
}
