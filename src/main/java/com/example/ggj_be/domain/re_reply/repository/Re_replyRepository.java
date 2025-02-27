package com.example.ggj_be.domain.re_reply.repository;

import com.example.ggj_be.domain.re_reply.Re_reply;
import com.example.ggj_be.domain.re_reply.dto.ReReplyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.ggj_be.domain.member.Member;

import java.util.List;


public interface Re_replyRepository extends JpaRepository<Re_reply, Long>{
    List<Re_reply> findByMember(Member member);


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
