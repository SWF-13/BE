package com.example.ggj_be.global.security;

import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberQueryService memberQueryService;

    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        Member member = memberQueryService.getMember(Long.parseLong(memberId));

        return new CustomUserDetails(member);
    }
}