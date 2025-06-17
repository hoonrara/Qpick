package com.quiz.quiz_app.repository;

import com.quiz.quiz_app.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByQuizSetId(Long quizSetId);
}
