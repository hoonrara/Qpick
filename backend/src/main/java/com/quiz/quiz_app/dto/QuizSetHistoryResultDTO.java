package com.quiz.quiz_app.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuizSetHistoryResultDTO {
    private Long quizSetId;
    private String title;
    private String imageUrl;
    private int correctCount;
    private int score;
    private LocalDateTime solvedAt;
}

