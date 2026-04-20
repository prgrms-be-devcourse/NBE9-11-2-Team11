package com.back.team11.domain.auth.controller;


import com.back.team11.domain.auth.service.AuthService;
import com.back.team11.domain.global.exception.CustomException;
import com.back.team11.domain.global.exception.ErrorCode;
import com.back.team11.domain.global.rsData.RsData;
import com.back.team11.domain.global.util.AuthUtil;
import com.back.team11.domain.member.dto.MemberResponseDto;
import com.back.team11.domain.member.entity.Member;
import com.back.team11.domain.member.repository.MemberRepository;
import com.back.team11.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/V1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthUtil authUtil;
    private final MemberService memberService;

    @PostMapping("/logout")
    public ResponseEntity<RsData<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ResponseEntity.ok(new RsData<>("로그아웃 성공", "200"));
    }

    @GetMapping("/me")
    public ResponseEntity<RsData<MemberResponseDto>> getMe() {
        Long memberId = authUtil.getCurrentMemberId();
        return ResponseEntity.ok(new RsData<>("내 정보 조회 성공", "200", memberService.getMe(memberId)));
    }
}
