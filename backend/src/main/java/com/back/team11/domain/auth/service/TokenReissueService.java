package com.back.team11.domain.auth.service;

import com.back.team11.domain.auth.entity.RefreshToken;
import com.back.team11.domain.auth.repository.RefreshTokenRepository;
import com.back.team11.domain.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenReissueService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResult reissue(String refreshTokenValue) {

        //  DB에서 RefreshToken 조회
        // 없으면 위조된 토큰으로 판단 → 예외 던짐
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        // 만료 여부 확인
        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("만료된 리프레시 토큰입니다.");
        }

        // 새 AccessToken + RefreshToken 발급
        //수정: Member 객체에서 id 꺼냄->  RefreshToken 엔티티가 memberId 대신 Member 객체를 들고 있어서?
        Long memberId = refreshToken.getMember().getId();;

        // 팀원 OAuth2 코드 머지 후 MemberRepository에서 role 조회로 변경 예정!
        String role = "USER";

        String newAccessToken = jwtTokenProvider.generateAccessToken(memberId, role);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        // DB에 새 RefreshToken 갱신 (rotate)
        LocalDateTime newExpiresAt = LocalDateTime.now()
                .plusSeconds(jwtTokenProvider.getRefreshTokenExpiration() / 1000);
        refreshToken.rotate(newRefreshToken, newExpiresAt);

        //  새 토큰 반환
        return new TokenResult(newAccessToken, newRefreshToken);
    }

     //재발급된 토큰을 담는 내부 클래스
     //Controller에서 꺼내서 쿠키에 담을 때 사용
    public record TokenResult(String accessToken, String refreshToken) {}
}