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
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**"
                        ).permitAll()

                        // [임시 추가] 테스트를 위해 관리자 카페 API 열어두기 위함
                        .requestMatchers(HttpMethod.POST, "/api/V1/admin/cafe/post").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/V1/admin/cafes").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/V1/admin/cafe/*").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/V1/admin/cafe/*").permitAll()


                        .requestMatchers(HttpMethod.GET,
                                "/api/*/cafe",
                                "/api/*/cafe/{id:\\d+}",
                                "/api/*/cafe/*/reviews"   //리뷰 조회 누구나 가능하게
                        ).permitAll()
                        .requestMatchers( // 홈, 에러, oauth 관련 경로 허용
                                "/",
                                "/login",
                                "/error",
                                "/api/V1/auth/oauth/**",
                                "/login/oauth2/**"
                        ).permitAll()
                        .requestMatchers("/api/*/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/*/**").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
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
}