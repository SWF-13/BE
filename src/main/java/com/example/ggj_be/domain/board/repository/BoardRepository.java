package com.example.ggj_be.domain.board.repository;

import com.example.ggj_be.domain.board.Board;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.board.dto.BoardHomeList;
import com.example.ggj_be.domain.board.dto.BoardDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByMember_UserId(Long userId);

    List<Board> findByMember(Member member);

    @Query(value = "SELECT " +

            "    b.board_id as boardId, " +
            "   b.created_at as createdAt, " +
            "    a.category_name as categoryName, " +
            "    a.category_id as categoryId, " +
            "    b.title, " +
            "    b.board_prize as boardPrize, " +
            "    IFNULL(c.good_count, 0) AS goodCount, " +
            "    IFNULL(d.reply_count, 0) AS replyCount, " +
            "    DATEDIFF(b.end_at, NOW()) AS endCount, " +
            "    e.nick_name as nickName, " +
            "CASE WHEN EXISTS (SELECT 1 " +
            "FROM good e " +
            "WHERE type = 'board' " +
            "AND user_id = :userId " +
            "AND b.board_id = e.object_id) " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END goodChk " +
            "FROM board b " +
            "JOIN category a ON a.category_id = b.category_id " +
            "JOIN member_tb e ON b.user_id = e.user_id " +
            "LEFT JOIN ( " +
            "    SELECT object_id, COUNT(*) AS good_Count " +
            "    FROM good " +
            "    WHERE type = 'board' " +
            "    GROUP BY object_id " +
            ") c ON b.board_id = c.object_id " +
            "LEFT JOIN ( " +
            "    SELECT a.board_id,  " +
            "COUNT(a.reply_id) + COALESCE(COUNT(b.reply_id), 0) AS reply_Count " +
            "FROM reply a " +
            "LEFT JOIN re_reply b ON a.reply_id = b.reply_id " +
            "GROUP BY a.board_id " +
            ") d ON b.board_id = d.board_id " +
            "WHERE b.acc_at IS NULL " +
            "	AND TIMESTAMPDIFF(SECOND, NOW(), b.end_at) > 0 " +
            "ORDER BY " +
            "    CASE " +
            "        WHEN :listType = 1 THEN b.end_at " +
            "        WHEN :listType = 2 THEN IFNULL(c.good_count, 0) " +
            "        WHEN :listType = 3 THEN b.board_prize " +
            "    END " , nativeQuery = true)
    List<BoardHomeList> findBoardHomeList(@Param("userId") Long userId, @Param("listType") int listType);

    @Query(value =
            "select " +
                "a.board_id as boardId, " +
                "b.nick_name as nickName, " +
                "b.user_img as userImg, " +
                "DATEDIFF(a.end_at, NOW()) AS endCount, " +
                "a.title, " +
                "c.category_name as categoryName, " +
                "c.category_id as categoryId, " +
                "CASE WHEN TIMESTAMPDIFF(DAY, a.created_at, NOW()) >= 1 " +
                "THEN CONCAT(TIMESTAMPDIFF(DAY, a.created_at, NOW()), '일 전') " +
                "WHEN TIMESTAMPDIFF(HOUR, a.created_at, NOW()) >= 1 " +
                "THEN CONCAT(TIMESTAMPDIFF(HOUR, a.created_at, NOW()), '시간 전') " +
                "ELSE CONCAT(TIMESTAMPDIFF(MINUTE, a.created_at, NOW()), '분 전') " +
                "END AS createdElapsed , " +
                "a.board_prize as boardPrize, " +
                "a.content , " +
                "CASE WHEN a.user_id = :userId THEN 1 ELSE 0 END isWriter, " +
                "CASE WHEN EXISTS (SELECT 1 FROM good d WHERE type = 'board' AND user_id = :userId AND a.board_id = d.object_id) " +
                     "THEN TRUE " +
                     "ELSE FALSE " +
                     "END goodChk, " +
                "CASE WHEN EXISTS (SELECT 1 FROM scrap e WHERE e.user_id = :userId AND e.board_id = :boardId) " +
                     "THEN TRUE " +
                     "ELSE FALSE " +
                     "END scrapChk , " +
                "( SELECT COALESCE(( SELECT COUNT(*) FROM good f WHERE type = 'board' and a.board_id = f.object_id GROUP BY object_id), 0)) as goodCount, " +
                "(SELECT  COUNT(g.reply_id) + COUNT(h.re_reply_id) FROM reply g LEFT JOIN re_reply h ON g.reply_id = h.reply_id WHERE a.board_id = g.board_id) replyCount , " +
                "( SELECT COALESCE((SELECT COUNT(*) FROM scrap i WHERE a.board_id = i.board_id GROUP BY board_id),0)) as scrapCount, " +
                "case when a.user_id = :userId " +
                     "then case when (CONVERT((SELECT COUNT(*) FROM reply j WHERE j.board_id = :boardId), SIGNED) <= 2) " +
                               "then 1 " +
                               "else 0 " +
                               "end " +
                               "else 0 " +
                     "end deleteChk,     " +
                " CASE WHEN a.acc_at IS NULL THEN 0 ELSE 1 END accChk " +
            "from board a " +
                " JOIN member_tb b ON a.user_id = b.user_id " +
                " JOIN category c ON a.category_id = c.category_id " +
            "where a.board_id = :boardId " +
                "and a.category_id = c.category_id "
            , nativeQuery = true)
    BoardDetail findBoardDetail(@Param("userId") Long userId, @Param("boardId") Long boardId);



    @Query(value = "SELECT " +
            "    b.board_id as boardId, " +
            "   b.created_at as createdAt, " +
            "    a.category_name as categoryName, " +
            "    a.category_id as categoryId, " +
            "    b.title, " +
            "    b.board_prize as boardPrize, " +
            "    IFNULL(c.good_count, 0) AS goodCount, " +
            "    IFNULL(d.reply_count, 0) AS replyCount, " +
            "    DATEDIFF(b.end_at, NOW()) AS endCount, " +
            "    e.nick_name as nickName , " +
            "CASE WHEN EXISTS (SELECT 1 " +
            "FROM good e " +
            "WHERE type = 'board' " +
            "AND user_id = :userId " +
            "AND b.board_id = e.object_id) " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END goodChk " +
            "FROM board b " +
            "JOIN category a ON a.category_id = b.category_id " +
            "JOIN member_tb e ON b.user_id = e.user_id " +
            "LEFT JOIN ( " +
            "    SELECT object_id, COUNT(*) AS good_Count " +
            "    FROM good " +
            "    WHERE type = 'board' " +
            "    GROUP BY object_id " +
            ") c ON b.board_id = c.object_id " +
            "LEFT JOIN ( " +
            "    SELECT a.board_id,  " +
            "COUNT(a.reply_id) + COALESCE(COUNT(b.reply_id), 0) AS reply_Count " +
            "FROM reply a " +
            "LEFT JOIN re_reply b ON a.reply_id = b.reply_id " +
            "GROUP BY a.board_id " +
            ") d ON b.board_id = d.board_id " +
            "WHERE b.acc_at IS NULL " +
            "	AND TIMESTAMPDIFF(SECOND, NOW(), b.end_at) > 0 " +
            "   AND (b.title LIKE CONCAT('%', :search, '%') OR a.category_name LIKE CONCAT('%', :search, '%')) " +
            "ORDER BY created_at desc"
            , nativeQuery = true)
    List<BoardHomeList> findSearchBoardList(@Param("userId") Long userId, @Param("search") String search);


    @Query(value = "SELECT " +
            "    b.board_id as boardId, " +
            "   b.created_at as createdAt, " +
            "    a.category_name as categoryName, " +
            "    a.category_id as categoryId, " +
            "    b.title, " +
            "    b.board_prize as boardPrize, " +
            "    IFNULL(c.good_count, 0) AS goodCount, " +
            "    IFNULL(d.reply_count, 0) AS replyCount, " +
            "    DATEDIFF(b.end_at, NOW()) AS endCount, " +
            "    e.nick_name as nickName, " +
            "CASE WHEN EXISTS (SELECT 1 " +
            "FROM good e " +
            "WHERE type = 'board' " +
            "AND user_id = :userId " +
            "AND b.board_id = e.object_id) " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END goodChk " +
            "FROM board b " +
            "JOIN category a ON a.category_id = b.category_id " +
            "JOIN member_tb e ON b.user_id = e.user_id " +
            "LEFT JOIN ( " +
            "    SELECT object_id, COUNT(*) AS good_Count " +
            "    FROM good " +
            "    WHERE type = 'board' " +
            "    GROUP BY object_id " +
            ") c ON b.board_id = c.object_id " +
            "LEFT JOIN ( " +
            "    SELECT a.board_id,  " +
            "COUNT(a.reply_id) + COALESCE(COUNT(b.reply_id), 0) AS reply_Count " +
            "FROM reply a " +
            "LEFT JOIN re_reply b ON a.reply_id = b.reply_id " +
            "GROUP BY a.board_id " +
            ") d ON b.board_id = d.board_id " +
            "WHERE b.acc_at IS NULL " +
            "	AND TIMESTAMPDIFF(SECOND, NOW(), b.end_at) > 0 " +
            "   AND a.category_id = :categoryId " +
            "ORDER BY created_at desc"
            , nativeQuery = true)
    List<BoardHomeList> findCategoryBoardList(@Param("userId") Long userId, @Param("categoryId") int categoryId);

    @Query("SELECT COUNT(g) FROM Good g WHERE g.objectId = :boardId AND g.type = 'BOARD'")
    Long countGoodsByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT " +
            "(SELECT COUNT(r) FROM Reply r WHERE r.board.boardId = :boardId) + " +
            "(SELECT COUNT(re) FROM Re_reply re WHERE re.reply.board.boardId = :boardId) AS totalCount " +
            "FROM Board b WHERE b.boardId = :boardId")
    Long countRepliesAndReRepliesByBoardId(@Param("boardId") Long boardId);

}