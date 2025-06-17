package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.Quiz;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResponseDTO {
    private Long quizId;
    private String title;
    private String imageUrl;

    public static QuizResponseDTO from(Quiz quiz) {
        return QuizResponseDTO.builder()
                .quizId(quiz.getId())
                .title(quiz.getTitle())
                .imageUrl(quiz.getImageUrl())
                .build();
    }
}
