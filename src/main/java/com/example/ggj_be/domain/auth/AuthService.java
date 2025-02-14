package com.example.ggj_be.domain.auth;

import com.example.ggj_be.domain.auth.dto.TokenVo;
import com.example.ggj_be.domain.member.Member;
import global.exception.ApiException;
import global.jwt.JwtProvider;
import global.response.code.status.ErrorStatus;
import global.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.ggj_be.domain.enums.Role;
import org.springframework.util.StringUtils;

import static global.util.JwtProperties.ACCESS_HEADER_STRING;
import static global.util.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;

    public TokenVo generateATAndRT(Member member) {
        String accessToken = jwtProvider.generateAccessToken(Long.valueOf(member.getAccountid()), member.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(Long.valueOf(member.getAccountid()),
                member.getRole());
        Long expiration = jwtProvider.getExpiration(refreshToken);

        log.info("===================== Add RefreshToken In Redis");
        redisUtil.set(member.getAccountid(), refreshToken, expiration);

        return new TokenVo(accessToken, refreshToken, member.getRole().toString());
    }

    public void logout(HttpServletRequest request) {

        String accessToken = extractAccessToken(request);
        Long expiration = jwtProvider.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "AT", expiration);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_HEADER_STRING);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(
                TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public TokenVo reIssueToken(String refreshToken) {

        Long employeeId = jwtProvider.getEmployeeId(refreshToken);
        Role role = jwtProvider.getRoleByToken(refreshToken);

        if (jwtProvider.checkRefreshTokenInRedis(employeeId, refreshToken)) {
            log.info("=========================== 만료된 Refresh Token");
            throw new ApiException(ErrorStatus._JWT_DIFF_REFRESH_TOKEN_IN_REDIS);
        }

        String newAccessToken = jwtProvider.generateAccessToken(employeeId, role);
        jwtProvider.getAuthentication(newAccessToken);

        String newRefreshToken = jwtProvider.generateRefreshToken(employeeId, role);
        Long expiration = jwtProvider.getExpiration(newRefreshToken);
        log.info("===================== reAdd RefreshToken In Redis");
        redisUtil.set(employeeId.toString(), newRefreshToken, expiration);

        return new TokenVo(newAccessToken, newRefreshToken, role.toString());
    }

}