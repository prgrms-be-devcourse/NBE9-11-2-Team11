package com.back.team11.domain.auth.controller;

import com.back.team11.domain.auth.dto.LoginRequestDto;
import com.back.team11.domain.auth.dto.TokenResponseDto;
import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.global.util.CookieUtil;
import com.back.team11.domain.member.entity.Member;
import com.back.team11.domain.member.service.MemberService;
import com.back.team11.domain.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<RsData<TokenResponseDto>> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {
        Member member = memberService.findByEmail(loginRequestDto.getEmail());

        if (member != null &&
                memberService.validatePassword(loginRequestDto.getPassword(), member.getPassword())) {

            String accessToken = jwtTokenProvider.generateAccessToken(
                    member.getId(),
                    member.getRole().toString()
            );

            String refreshToken = jwtTokenProvider.generateRefreshToken(member.getId());

            // 여기 변경
            cookieUtil.addAccessTokenCookie(response, accessToken);
            cookieUtil.addRefreshTokenCookie(response, refreshToken);

            return ResponseEntity.ok(
                    new RsData<>(
                            "로그인이 성공적으로 되었습니다.",
                            "200-1",
                            new TokenResponseDto(accessToken, refreshToken)
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RsData<>("Unauthorized", "401-1", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RsData<TokenResponseDto>> refresh(@CookieValue("accessToken") String refreshToken) {
        if (refreshToken != null) {
            Long memberId = jwtTokenProvider.getMemberId(refreshToken);
            String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, "ADMIN"); // "ADMIN"은 예시로 사용

            // 리프레시 토큰과 새로운 액세스 토큰을 함께 반환
            return ResponseEntity.ok(new RsData<>("토큰 리프레쉬가 성공적으로 되었습니다.", "200-1", new TokenResponseDto(newAccessToken, refreshToken)));
        }

        // 리프레시 토큰이 없을 경우
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RsData<>("리프레쉬 토큰이 존재하지 않거나 유효하지 않습니다.", "401-1", null)); // 실패 시에는 null
    }

    @PostMapping("/logout")
    public ResponseEntity<RsData<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0); // 쿠키 삭제
        cookie.setPath("/");

        response.addCookie(cookie);

        // 로그아웃 성공 시
        return ResponseEntity.ok(new RsData<>("로그아웃이 성공적으로 되었습니다.", "200-1", null)); // 로그아웃은 데이터가 필요하지 않으므로 null
    }
}