package com.back.team11.domain.auth.controller;

import com.back.team11.domain.auth.dto.LoginRequestDto;
import com.back.team11.domain.auth.service.AuthService;
import com.back.team11.domain.auth.service.TokenReissueService;
import com.back.team11.domain.global.rsData.RsData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/V1/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final TokenReissueService tokenReissueService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<RsData<Void>> login(
            @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response
    ) {
        authService.adminLogin(loginRequestDto, response);

        return ResponseEntity.ok(
                new RsData<>(
                        "관리자 로그인이 성공적으로 되었습니다.",
                        "200",
                        null
                )
        );
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
                        "200",
                        null
                )
        );
    }
}