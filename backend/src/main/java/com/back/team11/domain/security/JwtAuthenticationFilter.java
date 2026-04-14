package com.back.team11.domain.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// 직접 추가
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.SignatureException;
import java.io.IOException;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();// 빈 주입 대신 직접 생성, 별도 빈 등록 불필요

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Authorization 헤더에서 "Bearer {토큰}" 추출
        String token = resolveToken(request);

        // 토큰이 존재하는 경우에만 검증 진행
        // 없으면 다음 필터로 넘김 → SecurityConfig에서 permitAll 여부 판단
        if (StringUtils.hasText(token)) {
            try {
                // 토큰에서 사용자 정보 추출
                Long memberId = jwtTokenProvider.getMemberId(token);
                String role = jwtTokenProvider.getRole(token);

                // Spring Security 인증 객체 생성
                // principal 자리에 memberId 저장
                // → 컨트롤러에서 @AuthenticationPrincipal Long memberId 로 꺼낼 수 있음
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                memberId,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + role))
                        );

                // SecurityContext에 인증 정보 저장
                // 이 줄 있어야 .authenticated() 경로 통과 가능
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                // Access Token 만료 → 프론트에서 재발급 API 호출해야 함
                sendErrorResponse(response, 401, "401-3", "만료된 토큰입니다.");
                return;

            } catch (SignatureException | MalformedJwtException e) {
                // 서명 불일치 or 토큰 형식 오류
                sendErrorResponse(response, 401, "401-2", "유효하지 않은 토큰입니다.");
                return;

            } catch (UnsupportedJwtException e) {
                // 지원하지 않는 JWT 형식
                sendErrorResponse(response, 401, "401-2", "유효하지 않은 토큰입니다.");
                return;

            } catch (Exception e) {
                // 그 외 모든 예외
                sendErrorResponse(response, 401, "401-2", "유효하지 않은 토큰입니다.");
                return;
            }
        }

        // 토큰 없거나 검증 성공 → 다음 필터로 전달
        filterChain.doFilter(request, response);
    }

    /**
     1. Authorization 헤더에서 토큰 추출
     2. "Bearer eyJhbGc..." → "eyJhbGc..." 만 잘라서 반환
     3. 헤더 없거나 Bearer 형식 아니면 null 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     1. 인증 실패 시 JSON 에러 응답
     2. 필터에서는 @ExceptionHandler가 동작 안 해서 직접 response에 작성해야 함
     */
    private void sendErrorResponse(HttpServletResponse response,
                                   int status,
                                   String resultCode,
                                   String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        response.getWriter().write(
                objectMapper.writeValueAsString(
                        Map.of("resultCode", resultCode, "msg", message)
                )
        );
    }
}