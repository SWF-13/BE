package com.example.ggj_be.domain.member.service;

import com.example.ggj_be.domain.auth.dto.AuthRequest;
import com.example.ggj_be.domain.member.Member;

public interface MemberQueryService {
    Member checkAccountIdAndPwd(AuthRequest.LoginRequest loginRequest);

    Member getMember (Long id);

    Member findMember(Member member);

    void deleteMember(Member member);

}
