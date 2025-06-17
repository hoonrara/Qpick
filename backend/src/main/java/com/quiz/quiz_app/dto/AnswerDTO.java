// ✅ AnswerDTO.java
package com.quiz.quiz_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.quiz.quiz_app.entity.Answer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnswerDTO {

    private Long quizId;
    private String title; // 퀴즈 제목
    private String selectedAnswer; // 유저 입력
    private String correctAnswer;
    private String imageUrl;

    @JsonProperty
    private boolean isCorrect;

    @Builder
    public AnswerDTO(Long quizId, String title, String selectedAnswer,String correctAnswer, boolean isCorrect, String imageUrl) {
        this.quizId = quizId;
        this.title = title;
        this.selectedAnswer = selectedAnswer;
        this.correctAnswer = correctAnswer;
        this.isCorrect = isCorrect;
        this.imageUrl = imageUrl;
    }

    public static AnswerDTO fromEntity(Answer answer) {
        return AnswerDTO.builder()
                .quizId(answer.getQuiz().getId())
                .title(answer.getQuiz().getTitle())
                .selectedAnswer(answer.getUserAnswer())
                .correctAnswer(answer.getQuiz().getCorrectAnswer())
                .isCorrect(answer.isCorrect())
                .imageUrl(answer.getQuiz().getImageUrl())
                .build();
    }
}