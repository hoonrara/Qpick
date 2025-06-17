package com.quiz.quiz_app.service;

import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.jwt.JwtTokenProvider;
import com.quiz.quiz_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String provider = request.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (provider.equals("naver")) {
            attributes = (Map<String, Object>) attributes.get("response");
        }

        String userId = extractUserId(provider, attributes);
        String nickname = extractNickname(provider, attributes);

        if (userId == null) {
            throw new OAuth2AuthenticationException("User ID is missing in provider response.");
        }

        if (nickname == null || nickname.isBlank()) {
            nickname = provider.toUpperCase() + "_USER_" + userId;
        }

        // 소셜 고유 식별자
        String socialKey = provider + "_" + userId;

        // 기존 유저 확인
        Optional<User> existingUser = userRepository.findBySocialId(socialKey);
        User user;

        if (existingUser.isPresent()) {
            // 기존 유저는 닉네임 유지
            user = existingUser.get();
        } else {
            // 신규 유저는 닉네임 중복 체크
            String uniqueNickname = generateUniqueNickname(nickname);

            user = userRepository.save(User.builder()
                            .username(uniqueNickname)
                            .socialId(socialKey)
                            .score(0)
                            .build()
            );
        }

        // ✅ JWT 토큰 발급
        String token = jwtTokenProvider.generateToken(user.getUsername());

        // ✅ attributes에 토큰 포함해서 전달
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("id", userId);
        customAttributes.put("username", user.getUsername());
        customAttributes.put("createdAt", user.getCreatedAt());
        customAttributes.put("token", token);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                customAttributes,
                "id"
        );
    }

    private String extractUserId(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao", "naver" -> attributes.get("id").toString();
            case "google" -> attributes.get("sub").toString();
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    private String extractNickname(String provider, Map<String, Object> attributes) {
        return switch (provider) {
            case "kakao" -> {
                try {
                    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    yield (String) profile.get("nickname");
                } catch (Exception e) {
                    yield null;
                }
            }
            case "naver", "google" -> (String) attributes.get("name");
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }

    private String generateUniqueNickname(String nickname) {
        String originalNickname = nickname;
        int i = 1;
        while (userRepository.existsByUsername(nickname)){
            nickname = originalNickname + "_" + i++;
        }
        return nickname;
    }

}
