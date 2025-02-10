package com.example.ggj_be.domain.auth;

import com.example.ggj_be.domain.auth.dto.SignUpRequest;
import com.example.ggj_be.domain.auth.dto.TokenVo;
import com.example.ggj_be.domain.member.Member;
import com.example.ggj_be.domain.member.repository.MemberRepository;
import com.example.ggj_be.global.exception.ApiException;
import com.example.ggj_be.global.jwt.JwtProvider;
import com.example.ggj_be.global.response.code.status.ErrorStatus;
import com.example.ggj_be.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ggj_be.domain.enums.Role;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

import static com.example.ggj_be.global.util.JwtProperties.ACCESS_HEADER_STRING;
import static com.example.ggj_be.global.util.JwtProperties.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenVo generateATAndRT(Member member) {
        String accessToken = jwtProvider.generateAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId(),
                member.getRole());
        Long expiration = jwtProvider.getExpiration(refreshToken);

        log.info("===================== Add RefreshToken In Redis");
        redisUtil.set(String.valueOf(member.getAccountid()), refreshToken, expiration);

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

        Long memberId = jwtProvider.getMemberId(refreshToken);
        Role role = jwtProvider.getRoleByToken(refreshToken);

        if (jwtProvider.checkRefreshTokenInRedis(memberId, refreshToken)) {
            log.info("=========================== 만료된 Refresh Token");
            throw new ApiException(ErrorStatus._JWT_DIFF_REFRESH_TOKEN_IN_REDIS);
        }

        String newAccessToken = jwtProvider.generateAccessToken(memberId, role);
        jwtProvider.getAuthentication(newAccessToken);

        String newRefreshToken = jwtProvider.generateRefreshToken(memberId, role);
        Long expiration = jwtProvider.getExpiration(newRefreshToken);
        log.info("===================== reAdd RefreshToken In Redis");
        redisUtil.set(memberId.toString(), newRefreshToken, expiration);

        return new TokenVo(newAccessToken, newRefreshToken, role.toString());
    }

    @Transactional
    public String signUp(SignUpRequest request) {
        // 이메일 중복 체크
        if (memberRepository.existsByAccountid(request.getAccountId())) {
            throw new ApiException(ErrorStatus._USER_DUPLICATE);
        }

        // 이메일 인증 코드 확인
        String storedAuthCode = redisUtil.getEmailCode(request.getAccountId());
        if (storedAuthCode == null || !storedAuthCode.equals(request.getAuthCode())) {
            throw new ApiException(ErrorStatus._MAIL_WRONG_CODE);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 회원 저장
        Member member = Member.builder()
                .accountid(request.getAccountId())
                .password(encodedPassword)
                .nameKo(request.getNameKo())
                .memberNo(request.getMemberNo())
                .userBirth(request.getUserBirth())
                .userImg(request.getUserImg())
                .agreeService(request.getAgreeService())
                .agreeInfo(request.getAgreeInfo())
                .role(Role.MEMBER) // 기본 권한 설정
                .joinDt(LocalDate.now())
                .build();

        memberRepository.save(member);

        // 이메일 인증 코드 삭제
        redisUtil.deleteEmailCode(request.getAccountId());

        return "회원가입이 성공적으로 완료되었습니다.";
    }
}

