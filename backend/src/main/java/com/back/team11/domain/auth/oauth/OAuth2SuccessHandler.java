package com.back.team11.domain.auth.oauth;

import com.back.team11.domain.auth.entity.RefreshToken;
import com.back.team11.domain.auth.repository.RefreshTokenRepository;
import com.back.team11.domain.global.util.CookieUtil;
import com.back.team11.domain.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

// OAuth2 로그인 성공 직후 JWT를 발급
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 로그인 성공한 사용자 정보 가져오기
        // CustomOAuth2UserService에서 customAttributes에 담아둔 memberId, role 꺼냄
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Long memberId = (Long) oAuth2User.getAttributes().get("memberId");
        String role = (String) oAuth2User.getAttributes().get("role");

        // Access Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(memberId, role);

        // Refresh Token 생성
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        // Refresh Token DB 저장
        // 기존에 있으면 rotate(), 없으면 새로 저장
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshTokenExpiration() / 1000);

        refreshTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        // 기존 토큰 있으면 새 토큰으로 교체
                        existing -> existing.rotate(refreshToken, expiresAt),
                        // 없으면 새로 저장
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .memberId(memberId)
                                        .token(refreshToken)
                                        .expiresAt(expiresAt)
                                        .build()
                        )
                );

        // 쿠키에 토큰 담기 (CookieUtil 사용)
        cookieUtil.addAccessTokenCookie(response, accessToken);
        cookieUtil.addRefreshTokenCookie(response, refreshToken);
    }
}