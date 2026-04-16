package com.back.team11.domain.auth.controller;

import com.back.team11.domain.auth.dto.LoginRequestDto;
import com.back.team11.domain.auth.dto.TokenResponseDto;
import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/V1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 처리: 로그인 시 Access Token 및 Refresh Token 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 로그인 검증 (예시)
        if ("admin".equals(loginRequestDto.getUsername()) && "adminpass".equals(loginRequestDto.getPassword())) {
            // JWT 토큰 발급
            String accessToken = jwtTokenProvider.generateAccessToken(1L, "ADMIN"); // 임의의 값
            String refreshToken = jwtTokenProvider.generateRefreshToken(1L); // 임의의 값

            // Refresh Token을 쿠키에 저장
            Cookie refreshTokenCookie = new Cookie("accessToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(60 * 60 * 24); // 1일
            refreshTokenCookie.setPath("/");

            // 응답에 쿠키 추가
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok(new TokenResponseDto(accessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("accessToken") String refreshToken) {
        if (refreshToken != null) {
            Long memberId = jwtTokenProvider.getMemberId(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, "ADMIN");
            return ResponseEntity.ok(new TokenResponseDto(newAccessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing or invalid.");
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<RsData<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 삭제
        cookie.setPath("/");

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}