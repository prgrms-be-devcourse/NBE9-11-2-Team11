package com.back.team11.domain.auth.oauth;

import com.back.team11.domain.member.entity.Provider;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Provider provider;
    private final String providerId;
    private final String email;
    private final String nickname;
    private final Map<String, Object> attributes;

    public OAuthAttributes(
            Provider provider,
            String providerId,
            String email,
            String nickname,
            Map<String, Object> attributes
    ){
            this.provider = provider;
            this.providerId = providerId;
            this.email = email;
            this.nickname = nickname;
            this.attributes = attributes;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes){
        return switch(registrationId.toLowerCase()){
            case "kakao"->ofKakao(attributes);
            case "naver"->ofNaver(attributes);
            case "google"->ofGoogle(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: " + registrationId);
        };
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes){
        Object idObj = attributes.get("id");
        String providerId = idObj != null ? String.valueOf(idObj) : null;

        Map<String, Object> kakaoAccount = getMap(attributes, "kakao_account");
        Map<String, Object> profile = getMap(kakaoAccount, "profile");

        String email = getString(kakaoAccount,"email");
        String nickname = getString(profile, "nickname");

        return new OAuthAttributes(
                Provider.KAKAO,
                providerId,
                email,
                nickname,
                attributes
        );

    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = getMap(attributes, "response");

        String providerId = getString(response, "id");
        String email = getString(response, "email");
        String nickname = getString(response, "nickname");

        return new OAuthAttributes(
                Provider.NAVER,
                providerId,
                email,
                nickname,
                attributes
        );
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        String providerId = getString(attributes, "sub");
        String email = getString(attributes, "email");
        String nickname = getString(attributes, "name");

        return new OAuthAttributes(
                Provider.GOOGLE,
                providerId,
                email,
                nickname,
                attributes
        );
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getMap(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        if (value == null) {
            return Map.of();
        }
        if (!(value instanceof Map<?, ?> map)) {
            return Map.of();
        }
        return (Map<String, Object>) map;
    }

    private static String getString(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value != null ? String.valueOf(value) : null;
    }
}
