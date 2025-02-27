package com.example.ggj_be.global.jwt;

import static com.example.ggj_be.global.util.JwtProperties.MEMBER_ID_KEY;
import static com.example.ggj_be.global.util.JwtProperties.ROLE;


import com.example.ggj_be.domain.enums.Role;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import com.example.ggj_be.global.security.CustomUserDetailService;
import com.example.ggj_be.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



@Component
public class JwtProvider {

    private final Key key;
    private final Long ACCESS_TOKEN_EXPIRE_TIME;
    private final Long REFRESH_TOKEN_EXPIRE_TIME;
    private final CustomUserDetailService customUserDetailService;
    private final RedisUtil redisUtil;

    public JwtProvider(@Value("${jwt.secret_key}") String secretKey,
                       @Value("${jwt.access_token_expire}") Long accessTokenExpire,
                       @Value("${jwt.refresh_token_expire}") Long refreshTokenExpire,
                       CustomUserDetailService customUserDetailService,
                       RedisUtil redisUtil) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpire;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpire;
        this.customUserDetailService = customUserDetailService;
        this.redisUtil = redisUtil;
    }

    /**
     * JWT header "alg" : "HS512" payload "id" : "memberId" payload "auth" : "EMPLOYEE/ADMIN"
     * payload "iat" : "123456789" payload "exp" : "123456789"
     */
    public String generateAccessToken(Long memberId, Role role) {

        Date expiredAt = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .claim(ROLE, role)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateRefreshToken(Long memberId, Role role) {

        Date expiredAt = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME);
        return Jwts.builder()
                .claim(MEMBER_ID_KEY, memberId)
                .claim(ROLE, role)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(expiredAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getMemberId(String token) {
        return parseClaims(token)
                .get(MEMBER_ID_KEY, Long.class);
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiException(ErrorStatus._JWT_INVALID);
        }
    }

    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return "VALID";
        } catch (ExpiredJwtException e) {
            return "EXPIRED";
        } catch (SignatureException | MalformedJwtException e) {
            return "INVALID";
        }
    }

    public void getAuthentication(String token) {
        Claims claims = parseClaims(token);

        UserDetails userDetails = customUserDetailService.loadUserByUsername(
                claims.get(MEMBER_ID_KEY, Long.class).toString());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, token,
                userDetails.getAuthorities());

        setContextHolder(authenticationToken);
    }

    private void setContextHolder(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public boolean checkRefreshTokenInRedis(Long employeeId, String receivedToken) {
        String existedToken = redisUtil.get(employeeId.toString());
        return receivedToken.equals(existedToken);
    }

    public Long getExpiration(String token) {

        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(token).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    public boolean checkBlackList(String accessToken) {
        return redisUtil.hasKeyBlackList(accessToken);
    }


    public Role getRoleByToken(String token) {
        return parseClaims(token)
                .get(ROLE, Role.class);
    }
}