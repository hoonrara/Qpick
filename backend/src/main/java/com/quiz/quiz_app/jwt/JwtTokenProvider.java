package com.quiz.quiz_app.jwt;


import com.quiz.quiz_app.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long TOKEN_VALID_TIME = 1000 * 60 * 60 * 24; // 24시간

    public JwtTokenProvider() {
        String secret = "mysecretkeymysecretkeymysecretkey"; // 256비트 이상으로 설정
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ 토큰에서 username 추출
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // ✅ 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SecurityException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
