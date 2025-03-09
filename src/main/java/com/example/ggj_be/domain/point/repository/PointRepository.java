package com.example.ggj_be.domain.point.repository;

import com.example.ggj_be.domain.enums.PointType;
import com.example.ggj_be.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface PointRepository extends JpaRepository<Point, Long>{
    List<Point> findByPointTypeAndAccAtIsNullOrderByCreatedAtAsc(PointType pointType);
    List<Point> findByMember_UserIdAndAccAtIsNotNull(Long userId);
    List<Point> findByMember_UserIdAndCreatedAtAfterAndAccAtIsNotNull(Long userId, LocalDateTime startDate);
}
