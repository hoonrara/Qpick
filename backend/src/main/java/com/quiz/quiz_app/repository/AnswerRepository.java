package com.quiz.quiz_app.repository;

import com.quiz.quiz_app.entity.Answer;
import com.quiz.quiz_app.entity.Quiz;
import com.quiz.quiz_app.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    /**
     * 특정 유저가 푼 모든 답변 조회
     */
    @EntityGraph(attributePaths = {"quiz"})
    List<Answer> findByUser(User user);

    /**
     * 특정 퀴즈에 대한 모든 사용자 답변 조회
     */
    List<Answer> findByQuiz(Quiz quiz);


    @EntityGraph(attributePaths = {"quiz", "quiz.quizSet"})
    @Query("select a from Answer a where a.user.id = :userId")
    List<Answer> findWithQuizSetByUserId(@Param("userId") Long userId);





    @Modifying
    @Transactional
    @Query("DELETE FROM Answer a WHERE a.user.id = :userId AND a.quiz.quizSet.id = :quizSetId")
    void deleteByUserIdAndQuizSetId(@Param("userId") Long userId, @Param("quizSetId") Long quizSetId);
}
