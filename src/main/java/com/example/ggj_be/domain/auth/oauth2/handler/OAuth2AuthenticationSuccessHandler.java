package com.example.ggj_be.domain.auth.oauth2.handler;



import com.example.ggj_be.domain.auth.AuthService;
import com.example.ggj_be.domain.auth.dto.TokenVo;
import com.example.ggj_be.domain.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.ggj_be.domain.auth.oauth2.service.OAuth2UserPrincipal;
import com.example.ggj_be.domain.auth.oauth2.user.OAuth2Provider;
import com.example.ggj_be.domain.auth.oauth2.user.OAuth2UserUnlinkManager;
import com.example.ggj_be.domain.auth.oauth2.util.CookieUtils;
import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.jwt.JwtProvider;
import com.example.ggj_be.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static com.example.ggj_be.domain.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static com.example.ggj_be.domain.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse("http://gjgj-front.s3-website.ap-northeast-2.amazonaws.com/home");
        ;
        log.info("redirect to " + targetUrl);

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {
            log.info("email={}, name={}, nickname={}, accessToken={}", principal.getUserInfo().getEmail(),
                    principal.getUserInfo().getName(),
                    principal.getUserInfo().getNickname(),
                    principal.getUserInfo().getAccessToken()
            );
            String email = principal.getUserInfo().getEmail();
            Optional<Member> existingMember = memberRepository.findByEmail(email);

            Member member;

            if (existingMember.isPresent()) {
                member = existingMember.get();
            } else {
                // 회원 저장

                member = Member.builder()
                        .accountid(principal.getUserInfo().getId())
                        .nickName(principal.getUserInfo().getNickname())
                        .email(email)
                        .password(principal.getPassword())
                        .nameKo(principal.getUserInfo().getName())
                        .userImg(principal.getUserInfo().getProfileImageUrl())
                        .agreeService(true)
                        .agreeInfo(true)
                        .role(Role.MEMBER) // 기본 권한 설정
                        .joinDt(LocalDate.now())
                        .build();
                memberRepository.save(member);

            }


            TokenVo tokenVo = authService.generateATAndRT(member);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", tokenVo.accessToken())
                    .queryParam("refresh_token", tokenVo.refreshToken())
                    .build().toUriString();

        } else if ("unlink".equalsIgnoreCase(mode)) {

            String accessToken = principal.getUserInfo().getAccessToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            oAuth2UserUnlinkManager.unlink(provider, accessToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}