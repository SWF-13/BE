package com.example.ggj_be.domain.board.repository;
import com.example.ggj_be.domain.board.dto.BoardSelecHomeListRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ggj_be.domain.board.Board;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT " +
            "    b.board_id, "+
            "    a.category_name, "+
            "    b.title, "+
            "    b.board_prize, "+
            "    IFNULL(c.good_count, 0) AS good_count, "+
            "    IFNULL(d.reply_count, 0) AS reply_count, "+
            "    DATEDIFF(b.end_at, NOW()) AS end_count, "+
            "CASE WHEN EXISTS (SELECT 1 " +
                              "FROM good e " +
                              "WHERE type = 'board' " +
                                "AND user_seq = :user_seq " +
                                "AND b.board_id = e.object_id) " +
                "THEN TRUE " +
                "ELSE FALSE " +
                "END good_chk " +
            "FROM board b "+
            "JOIN category a ON a.category_id = b.category_id "+
            "LEFT JOIN ( "+
            "    SELECT object_id, COUNT(*) AS good_count "+
            "    FROM good "+
            "    WHERE type = 'board' "+
            "    GROUP BY object_id "+
            ") c ON b.board_id = c.object_id "+
            "LEFT JOIN ( "+
            "    SELECT  a.board_id, "+
            "		COUNT(a.reply_id) + COUNT(b.reply_id) AS reply_count "+
            "	FROM reply a "+
            "		, re_reply b "+
            "	WHERE a.reply_id = b.reply_id "+
            "	GROUP BY a.board_id "+
            ") d ON b.board_id = d.board_id "+
            "WHERE b.acc_at IS NULL "+
            "	AND TIMESTAMPDIFF(SECOND, NOW(), b.end_at) > 0 "+
            "ORDER BY " +
            "    CASE " +
            "        WHEN :list_type = 1 THEN b.end_at " +
            "        WHEN :list_type = 2 THEN IFNULL(c.good_count, 0) " +
            "        WHEN :list_type = 3 THEN b.board_prize " +
            "    END " +
            "LIMIT 10", nativeQuery = true)
    List<BoardSelecHomeListRequest> findBoardSelectEndRequest(@Param("user_seq") Long user_seq, @Param("list_type") int list_type);
}
