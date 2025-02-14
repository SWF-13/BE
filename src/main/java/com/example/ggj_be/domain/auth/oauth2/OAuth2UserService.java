package com.example.ggj_be.domain.auth.oauth2;

import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttribute = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo userInfo = OAuth2UserInfo.of(registrationId, attributes);

        // DB에 사용자 저장 또는 업데이트
        Member member = saveOrUpdate(userInfo);

        return new CustomOAuth2User(member, attributes);
    }

    private Member saveOrUpdate(OAuth2UserInfo userInfo) {
        Optional<Member> optionalMember = memberRepository.findByAccountid(userInfo.getId());

        if (optionalMember.isPresent()) {
            return optionalMember.get();
        }

        Member member = Member.builder()
                .nameKo(userInfo.getName())
                .role(Role.MEMBER)
                .email(userInfo.getEmail())
                .build();
        return memberRepository.save(member);
    }
}
