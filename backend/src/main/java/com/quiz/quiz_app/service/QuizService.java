package com.quiz.quiz_app.service;

import com.quiz.quiz_app.dto.QuizRequestDTO;
import com.quiz.quiz_app.dto.QuizResponseDTO;
import com.quiz.quiz_app.entity.Quiz;
import com.quiz.quiz_app.entity.QuizSet;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.QuizRepository;
import com.quiz.quiz_app.repository.QuizSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSetRepository quizSetRepository;

    /**
     * [퀴즈 10개 일괄 등록]
     */
    @Transactional
    public void createQuizBatch(Long quizSetId, List<QuizRequestDTO> requests, User user) {
        QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈 세트를 찾을 수 없습니다. id=" + quizSetId));

        List<Quiz> quizzes = requests.stream()
                .map(dto -> dto.toEntity(quizSet, user))
                .collect(Collectors.toList());

        quizRepository.saveAll(quizzes);
    }




    /**
     * [퀴즈셋 기준 전체 퀴즈 조회]
     * - 사용자가 퀴즈셋 클릭 시 전체 퀴즈 목록 불러올 때 사용 가능
     */
    public List<QuizResponseDTO> getQuizzesBySet(Long quizSetId) {
        QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(() -> new IllegalArgumentException("해당 퀴즈 세트가 없습니다. id=" + quizSetId));
        return quizSet.getQuizzes().stream()
                .map(QuizResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * [전체 퀴즈 조회]
     * - 전체 퀴즈 보기용 (예: 관리자 페이지 등)
     */
    public List<QuizResponseDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(QuizResponseDTO::from)
                .collect(Collectors.toList());
    }
}
