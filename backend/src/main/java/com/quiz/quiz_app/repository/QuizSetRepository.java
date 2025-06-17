package com.quiz.quiz_app.repository;

import com.quiz.quiz_app.entity.QuizSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {
    // ✅ 유저가 만든 퀴즈셋 목록
    List<QuizSet> findByCreatorId(Long creatorId);
    List<QuizSet> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

}
