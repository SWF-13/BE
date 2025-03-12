package com.example.ggj_be.domain.common.repository;

import com.example.ggj_be.domain.common.Good;
import com.example.ggj_be.domain.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



public interface GoodRepository extends JpaRepository<Good, Long>{
    Optional<Good> findByObjectIdAndType(Long objectId, Type type);
}
