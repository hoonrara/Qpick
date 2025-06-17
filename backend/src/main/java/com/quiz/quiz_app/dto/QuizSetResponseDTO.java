package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.QuizSet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSetResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;

    public static QuizSetResponseDTO from(QuizSet set) {
        return QuizSetResponseDTO.builder()
                .id(set.getId())
                .title(set.getTitle())
                .description(set.getDescription())
                .imageUrl(set.getImageUrl())
                .build();
    }
}
