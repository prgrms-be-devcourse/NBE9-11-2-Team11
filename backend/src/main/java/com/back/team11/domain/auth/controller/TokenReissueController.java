package com.back.team11.domain.auth.controller;

import com.back.team11.domain.auth.service.TokenReissueService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import com.back.team11.domain.global.rsData.RsData;


//Access Token 재발급 API
@RestController
@RequestMapping("/api/V1/auth")
@RequiredArgsConstructor
public class TokenReissueController {

    private final TokenReissueService tokenReissueService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request,
                                     HttpServletResponse response) {

        // 쿠키에서 RefreshToken 꺼내기
        // 쿠키가 없거나 refreshToken 쿠키가 없으면 예외 던짐
        String refreshTokenValue = extractRefreshTokenFromCookie(request);

        // TokenReissueService 호출 → 새 토큰 발급
        TokenReissueService.TokenResult tokenResult = tokenReissueService.reissue(refreshTokenValue);

        //  새 AccessToken 쿠키에 담기
        // HttpOnly false -> 프론트 JS에서 읽어서 Authorization 헤더에 담아야 하므로
        Cookie accessTokenCookie = new Cookie("accessToken", tokenResult.accessToken());
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(30 * 60); // 30분
        response.addCookie(accessTokenCookie);

        //  새 RefreshToken HttpOnly 쿠키에 담기
        // HttpOnly true -> JS에서 접근 못하게 막아서 보안 강화
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenResult.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshTokenCookie);

        //  응답 반환
        return ResponseEntity.ok(new RsData<>("토큰 재발급 성공", "200"));
    }

     //쿠키에서 RefreshToken 추출
     //쿠키 없거나 refreshToken 없으면 예외 던짐
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new IllegalArgumentException("쿠키가 없습니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰 쿠키가 없습니다."));
    }
}