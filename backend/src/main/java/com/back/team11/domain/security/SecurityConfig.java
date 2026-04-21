package com.back.team11.domain.security;

import com.back.team11.domain.auth.oauth.CustomOAuth2UserService;
import com.back.team11.domain.auth.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 인증 필터
    private final JwtAuthenticationFilter jwtAuthenticationFilter; //주석 해제

    // OAuth2 로그인 성공 후 JWT 발급 핸들러
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // 주석 해제
    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * HTTP 요청에 대한 보안 필터 체인 설정
     * 인증/인가 규칙, 세션 정책, CSRF, 예외 처리 등을 정의
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI — 개발/문서화 도구, 인증 없이 접근 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // 현재 로그인한 사용자 정보 조회 — JWT 쿠키 기반 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/V1/auth/me").authenticated()
                        // 카카오 OAuth 로그아웃 — 인증된 사용자만 로그아웃 가능
                        .requestMatchers(HttpMethod.POST, "/api/V1/auth/logout").authenticated()

                        // 카페 목록/상세/리뷰 조회 — 비로그인 사용자도 열람 가능
                        .requestMatchers(HttpMethod.GET, "/api/V1/cafe").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/V1/cafe/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/V1/cafe/*/reviews").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/V1/cafe/*/reviews/page").permitAll()

                        // 홈, 에러, Kakao OAuth 인증 흐름 관련 경로 — 인증 전에 접근되는 경로
                        .requestMatchers(
                                "/",
                                "/login",
                                "/error",
                                "/api/V1/auth/oauth/**",
                                "/login/oauth2/**"
                        ).permitAll()

                        // 관리자 로그인 — username/password 방식이므로 인증 전 접근 허용
                        .requestMatchers("/api/V1/admin/auth/login").permitAll()
                        // 관리자 전용 API — ROLE_ADMIN 권한 필요 (카페 등록/수정/삭제/승인/거부 포함)
                        .requestMatchers("/api/V1/admin/**").hasRole("ADMIN")
                        // 그 외 모든 /api/** 경로 — 로그인한 사용자만 접근 가능
                        .requestMatchers("/api/*/**").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/api/V1/auth/oauth") //OAuth 시작 엔드포인트 설정
                        )
                        .redirectionEndpoint(redirection -> redirection // OAuth 제공자가 로그인 후 redirect 해주는 콜백 URL
                                .baseUri("/api/V1/auth/oauth/*/callback")
                        )
                        .defaultSuccessUrl("/loginSuccess", true)
                        .failureUrl("/login?error")
                        // OAuth 제공자로부터 받은 사용자 정보를 어떻게 처리할지 설정
                        // 여기서 CustomOAuth2UserService가 실행
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)  // 주석 해제
                        .failureHandler((request, response, exception) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(401);
                            response.getWriter().write("""
                                     {"resultCode": "401-1", "msg": "소셜 로그인에 실패했습니다."}
                                     """);
                        })
                )

                // Spring 기본 로그인 필터 앞에 JWT 필터를 먼저 실행
                // → 모든 요청에서 JWT 토큰 유효성을 먼저 검사
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // 주석 해제

                // 인증/인가 실패 시 커스텀 에러 응답 설정
                .exceptionHandling(exception -> exception
                        // 인증 실패 (토큰 없음 / 만료 등) → 401 응답
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(401);
                            response.getWriter().write("""
                                    {"resultCode": "401-1", "msg": "로그인 후 이용해주세요."}
                                    """);
                        })
                        // 권한 부족 (로그인은 됐지만 접근 불가) → 403 응답
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(403);
                            response.getWriter().write("""
                                    {"resultCode": "403-1", "msg": "접근 권한이 없습니다."}
                                    """);
                        })
                );
        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));   // 현재 로컬 개발 환경만 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 허용할 HTTP 메서드 목록
        configuration.setAllowedHeaders(List.of("*")); // 모든 요청 헤더 허용
        configuration.setAllowCredentials(true);  // 쿠키/인증 정보 포함 요청 허용

        // /api/** 경로에 위 CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    // PasswordEncoder를 Spring Security에 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}