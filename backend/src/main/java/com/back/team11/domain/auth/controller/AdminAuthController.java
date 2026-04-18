package com.back.team11.domain.auth.controller;

import com.back.team11.domain.auth.dto.LoginRequestDto;
import com.back.team11.domain.auth.dto.TokenResponseDto;
import com.back.team11.domain.auth.service.AuthService;
import com.back.team11.domain.auth.service.TokenReissueService;
import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.global.util.CookieUtil;
import com.back.team11.domain.member.entity.Member;
import com.back.team11.domain.member.service.MemberService;
import com.back.team11.domain.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenReissueService tokenReissueService;
    private final AuthService authService;

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
                            "200",
                            null
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new RsData<>("Unauthorized", "401-1", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RsData<Void>> refresh(HttpServletRequest request,
                                                HttpServletResponse response) {

        tokenReissueService.reissue(request, response);

        // 리프레시 토큰이 없을 경우
        return ResponseEntity.ok(new RsData<>("토큰 재발급이 성공적으로 되었습니다.","200"));
    }

    @PostMapping("/logout")
    public ResponseEntity<RsData<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);

        return ResponseEntity.ok(
                new RsData<>(
                        "로그아웃이 성공적으로 되었습니다.",
                        "200-1",
                        null
                )
        );
    }
}