package com.example.ggj_be.domain.common.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ggj_be.domain.common.Poto;
import java.util.List;
import com.example.ggj_be.domain.enums.Type;

public interface PotoRepository extends JpaRepository<Poto, Long>{
    List<Poto> findByTypeAndObjectId(Type type, Long objectId);
    List<Poto> findByObjectIdAndType(Long objectId, Type type);
}
