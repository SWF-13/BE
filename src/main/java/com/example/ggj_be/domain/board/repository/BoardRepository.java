package com.example.ggj_be.domain.board.repository;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.board.dto.BoardSelectEndRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ggj_be.domain.board.Board;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {
//    List<Board> findByMember_User_seq(Long user_seq);
    List<Board> findByMember_UserId(Long userId);




    @Query(value = "SELECT " +
            "    a.category_name, "+
            "    b.title, "+
            "    IFNULL(c.good_count, 0) AS good_count, "+
            "    IFNULL(d.reply_count, 0) AS reply_count, "+
            "    DATEDIFF(b.end_at, NOW()) AS end_count "+
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
            "ORDER BY b.end_at ASC "+
            "LIMIT 5", nativeQuery = true)
    List<BoardSelectEndRequest> findBoardSelectEndRequest();
}
