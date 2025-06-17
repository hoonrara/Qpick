package com.quiz.quiz_app.entity;

import com.quiz.quiz_app.entity.QuizSet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 🟡 문제 제목 (필수)

    @Column(nullable = false)
    private String imageUrl; // 🟡 이미지 문제

    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_set_id")
    private QuizSet quizSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy; // ✅ 추가됨

    @Builder
    public Quiz(String title, String imageUrl, String correctAnswer, QuizSet quizSet, User createdBy) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.correctAnswer = correctAnswer;
        this.quizSet = quizSet;
        this.createdBy = createdBy;
    }
}
