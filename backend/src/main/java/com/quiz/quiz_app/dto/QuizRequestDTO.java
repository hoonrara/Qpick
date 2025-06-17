package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.Quiz;
import com.quiz.quiz_app.entity.QuizSet;
import com.quiz.quiz_app.entity.User;
import lombok.Getter;

@Getter
public class QuizRequestDTO {
    private String title;
    private String imageUrl;
    private String answer;

    public Quiz toEntity(QuizSet quizSet, User user) {
        return Quiz.builder()
                .title(title)
                .imageUrl(imageUrl)
                .correctAnswer(answer)
                .quizSet(quizSet)
                .createdBy(user) // ✅ 추가
                .build();
    }
}
