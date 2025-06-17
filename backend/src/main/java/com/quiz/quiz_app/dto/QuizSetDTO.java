package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.QuizSet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuizSetDTO {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long userId; // ✅ 만든 유저의 ID

    @Builder
    public QuizSetDTO(Long id, String title, String description, String imageUrl, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }

    // ✅ Entity → DTO로 변환
    public static QuizSetDTO from(QuizSet entity) {
        return QuizSetDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .userId(entity.getCreator() != null ? entity.getCreator().getId() : null)  // 여기 추가
                .build();
    }
}
