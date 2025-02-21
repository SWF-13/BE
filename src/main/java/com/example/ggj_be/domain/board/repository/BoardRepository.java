package com.example.ggj_be.domain.board.repository;
import com.example.ggj_be.domain.board.dto.BoardHomeList;
import com.example.ggj_be.domain.board.dto.BoardDetail;
import com.example.ggj_be.domain.board.dto.ReReplyDetail;
import com.example.ggj_be.domain.board.dto.ReplyDetail;
import com.example.ggj_be.domain.common.Poto;
import com.example.ggj_be.domain.reply.Reply;
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
            "   b.created_at, "+
            "    a.category_name, "+
            "    b.title, "+
            "    b.board_prize, "+
            "    IFNULL(c.good_count, 0) AS good_count, "+
            "    IFNULL(d.reply_count, 0) AS reply_count, "+
            "    DATEDIFF(b.end_at, NOW()) AS end_count, "+
            "    e.nick_name , " +
            "CASE WHEN EXISTS (SELECT 1 " +
                              "FROM good e " +
                              "WHERE type = 'board' " +
                                "AND user_id = :userId " +
                                "AND b.board_id = e.object_id) " +
                "THEN TRUE " +
                "ELSE FALSE " +
                "END good_chk " +
            "FROM board b "+
            "JOIN category a ON a.category_id = b.category_id "+
            "JOIN member_tb e ON b.user_id = e.user_id " +
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
            "        WHEN :listType = 1 THEN b.end_at " +
            "        WHEN :listType = 2 THEN IFNULL(c.good_count, 0) " +
            "        WHEN :listType = 3 THEN b.board_prize " +
            "    END " +
            "LIMIT 10", nativeQuery = true)
    List<BoardHomeList> findBoardHomeList(@Param("userId") Long userId, @Param("listType") int listType);

    @Query(value =
            "select " +
                "a.board_id, " +
                "b.nick_name, " +
                "b.user_img, "+
                "DATEDIFF(a.end_at, NOW()) AS end_count, "+
                "a.title, "+
                "c.category_name, "+
                "CASE WHEN TIMESTAMPDIFF(DAY, a.created_at, NOW()) >= 1 "+
                    "THEN CONCAT(TIMESTAMPDIFF(DAY, a.created_at, NOW()), '일 전') "+
                    "WHEN TIMESTAMPDIFF(HOUR, a.created_at, NOW()) >= 1 "+
                    "THEN CONCAT(TIMESTAMPDIFF(HOUR, a.created_at, NOW()), '시간 전') "+
                    "ELSE CONCAT(TIMESTAMPDIFF(MINUTE, a.created_at, NOW()), '분 전') "+
                    "END AS created_elapsed , "+
                "a.board_prize , "+
                "a.content , "+
                "CASE WHEN a.user_id = :userId THEN 1 ELSE 0 END is_writer, "+
                "CASE WHEN EXISTS (SELECT 1 "+
                                  "FROM good d "+
                                  "WHERE type = 'board' "+
                                      "AND user_id = :userId "+
                                      "AND a.board_id = d.object_id) "+
                    "THEN TRUE "+
                    "ELSE FALSE "+
                    "END good_chk, "+
                "CASE WHEN EXISTS (SELECT 1 "+
                                  "FROM scrap e "+
                                  "WHERE user_id = :userId "+
                                    "AND a.board_id = :boardId) "+
                    "THEN TRUE "+
                    "ELSE FALSE "+
                    "END scrap_chk , "+
                "( SELECT COALESCE(( SELECT COUNT(*) "+
                    "FROM good f "+
                    "WHERE type = 'board' "+
                      "and a.board_id = f.object_id "+
                    "GROUP BY object_id), 0)) as good_count, "+
                "(SELECT  COUNT(g.reply_id) + COUNT(h.reply_id) "+
                    "FROM reply g  "+
                    ", re_reply h "+
                    "WHERE g.reply_id = h.reply_id "+
                    "AND a.board_id = g.board_id) reply_count , "+
                "( SELECT COALESCE((SELECT COUNT(*) "+
                    "FROM scrap i "+
                    "WHERE a.board_id = i.board_id "+
                    "GROUP BY board_id),0)) as scrap_count "+
            "from board a , "+
                "(select user_id , nick_name, user_img from member_tb where user_id = :userId)b "+
                ", category c "+
            "where a.board_id = :boardId "+
                "and a.category_id = c.category_id "
            , nativeQuery = true)
    BoardDetail findBoardDetail(@Param("userId") Long userId, @Param("boardId") Long boardId);


    @Query(value =
            "select "+
                "a.reply_id, "+
                "CASE WHEN a.acc_at IS NOT NULL THEN 1 ELSE 0 END AS acc_chk , "+
                "a.content, "+
                "b.nick_name, "+
                "b.user_img,  "+
                "CASE WHEN a.user_id = :userId THEN 1 ELSE 0 END is_writer , "+
                "CASE WHEN EXISTS (SELECT 1 "+
                                  "FROM good c "+
                                  "WHERE type = 'reply' "+
                                    "AND user_id = :userId "+
                                    "AND a.reply_id = c.object_id) "+
                    "THEN 1 "+
                    "ELSE 0 "+
                    "END good_chk,  "+
                "( SELECT COALESCE(( SELECT COUNT(*) "+
                                    "FROM good d "+
                                    "WHERE type = 'reply' "+
                                        "and a.reply_id = d.object_id "+
                                    "GROUP BY object_id), 0)) as good_count "+
            "from reply a "+
                ", member_tb b "+
            "where a.board_id = :boardId "+
                "and a.user_id = b.user_id "
            , nativeQuery = true)
    List<ReplyDetail> findReplyDetail(@Param("userId") Long userId, @Param("boardId") Long boardId);

    @Query(value =
            "select "+
                "a.re_reply_id, "+
                "a.content, "+
                "b.nick_name, "+
                "b.user_img,  "+
                "CASE WHEN a.user_id = :userId THEN 1 ELSE 0 END is_writer , "+
                "CASE WHEN EXISTS (SELECT 1 "+
                                  "FROM good c "+
                                  "WHERE type = 'reReply' "+
                                    "AND user_id = :userId "+
                                    "AND a.re_reply_id = c.object_id) "+
                    "THEN 1 "+
                    "ELSE 0 "+
                    "END good_chk,  "+
                "( SELECT COALESCE(( SELECT COUNT(*) "+
                  "FROM good d "+
                  "WHERE type = 'reReply' "+
                    "and a.re_reply_id = d.object_id "+
                  "GROUP BY object_id), 0)) as good_count "+
            "from re_reply a "+
            ", member_tb b "+
            "where a.reply_id = :replyId "+
                "and a.user_id = b.user_id "
            , nativeQuery = true)
    List<ReReplyDetail> findReReplyDetail(@Param("userId") Long userId, @Param("replyId") Long replyId);
}
