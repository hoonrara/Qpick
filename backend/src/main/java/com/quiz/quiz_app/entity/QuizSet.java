package com.quiz.quiz_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class QuizSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String imageUrl; // 대표 이미지 URL

    @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quiz> quizzes = new ArrayList<>();

    // ✅ creator 추가
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Builder
    public QuizSet(String title, String description, String imageUrl, User creator) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.creator = creator;
    }
}
