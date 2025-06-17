package com.quiz.quiz_app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Entity를 구분하기 위한 id

    @Column(nullable = false, unique = true)
    private String username; // 닉네임

    @Column(unique = true)
    private String socialId;

    private int score = 0; // 점수의 초기값 0으로 설정

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Builder 패턴으로 User 생성자 생성
    @Builder
    public User(String username, String socialId, int score, LocalDateTime createdAt){
        this.username = username;
        this.socialId = socialId;
        this.score = score;
        this.createdAt = createdAt;
    }

}