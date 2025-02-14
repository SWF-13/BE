package com.example.ggj_be.global.config;

import com.example.ggj_be.domain.auth.oauth2.CustomOAuth2User;
import com.example.ggj_be.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(oAuth2User.getMember().getUserId(), oAuth2User.getMember().getRole());

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect("http://localhost:3000/oauth-success?token=" + accessToken);
    }
}
