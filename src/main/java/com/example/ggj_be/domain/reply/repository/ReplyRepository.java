package com.example.ggj_be.domain.reply.repository;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.reply.Reply;
import com.example.ggj_be.domain.reply.dto.ReplyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long>{
    List<Reply> findByMember_UserId(Long userId);

    List<Reply> findByMember(Member member);

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
}
