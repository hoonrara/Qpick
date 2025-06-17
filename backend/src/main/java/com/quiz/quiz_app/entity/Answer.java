package com.quiz.quiz_app.entity;

import com.quiz.quiz_app.entity.Quiz;
import com.quiz.quiz_app.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userAnswer; // 유저가 작성한 문자열 정답

    @Column(nullable = false)
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder
    public Answer(String userAnswer, boolean isCorrect, Quiz quiz, User user) {
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
        this.quiz = quiz;
        this.user = user;
    }
}
