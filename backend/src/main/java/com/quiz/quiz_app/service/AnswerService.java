package com.quiz.quiz_app.service;

import com.quiz.quiz_app.dto.AnswerDTO;
import com.quiz.quiz_app.dto.AnswerResultDTO;
import com.quiz.quiz_app.dto.AnswerSubmitDTO;
import com.quiz.quiz_app.dto.QuizSetHistoryResultDTO;
import com.quiz.quiz_app.entity.Answer;
import com.quiz.quiz_app.entity.Quiz;
import com.quiz.quiz_app.entity.QuizSet;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.AnswerRepository;
import com.quiz.quiz_app.repository.QuizRepository;
import com.quiz.quiz_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    /**
     * [정답 제출 및 채점 - 기존 기록 덮어쓰기 방식]
     */
    @Transactional
    public AnswerResultDTO submitAnswers(Long userId, List<AnswerSubmitDTO> submitList) {
        if (submitList.isEmpty()) {
            throw new IllegalArgumentException("제출된 정답이 없습니다.");
        }

        final User user = (userId != 0L)
                ? userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."))
                : null;

        List<Long> quizIds = submitList.stream()
                .map(AnswerSubmitDTO::getQuizId)
                .distinct()
                .toList();

        Map<Long, Quiz> quizMap = quizRepository.findAllById(quizIds).stream()
                .collect(Collectors.toMap(Quiz::getId, quiz -> quiz));

        QuizSet quizSet = quizMap.get(quizIds.get(0)).getQuizSet();

        if (user != null) {
            answerRepository.deleteByUserIdAndQuizSetId(user.getId(), quizSet.getId());
        }

        List<Answer> answers = submitList.stream()
                .map(submit -> {
                    Quiz quiz = quizMap.get(submit.getQuizId());
                    if (quiz == null) {
                        throw new IllegalArgumentException("해당 퀴즈가 없습니다.");
                    }

                    boolean isCorrect = quiz.getCorrectAnswer().equalsIgnoreCase(submit.getUserAnswer().trim());

                    return Answer.builder()
                            .quiz(quiz)
                            .user(user)
                            .userAnswer(submit.getUserAnswer())
                            .isCorrect(isCorrect)
                            .build();
                })
                .toList();

        answerRepository.saveAll(answers);
        return AnswerResultDTO.from(answers);
    }



    /**
     * [유저별 전체 풀이 기록 조회]
     */
    public List<AnswerDTO> getAnswersByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        return answerRepository.findByUser(user).stream()
                .map(AnswerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * [퀴즈 단일 기록 조회]
     */
    public List<AnswerDTO> getAnswersByQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈가 존재하지 않습니다."));
        return answerRepository.findByQuiz(quiz).stream()
                .map(AnswerDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * [퀴즈셋 단위로 최근 풀이 기록 요약 조회]
     */
    public List<QuizSetHistoryResultDTO> getQuizSetHistory(Long userId) {
        List<Answer> answers = answerRepository.findWithQuizSetByUserId(userId);

        return answers.stream()
                .collect(Collectors.groupingBy(a -> a.getQuiz().getQuizSet()))
                .entrySet().stream()
                .map(entry -> {
                    QuizSet set = entry.getKey();
                    List<Answer> list = entry.getValue();
                    int correct = (int) list.stream().filter(Answer::isCorrect).count();
                    LocalDateTime time = list.stream().map(Answer::getCreatedAt).max(LocalDateTime::compareTo).orElse(LocalDateTime.now());

                    return QuizSetHistoryResultDTO.builder()
                            .quizSetId(set.getId())
                            .title(set.getTitle())
                            .imageUrl(set.getImageUrl())
                            .correctCount(correct)
                            .score(correct * 10)
                            .solvedAt(time)
                            .build();
                })
                .sorted(Comparator.comparing(QuizSetHistoryResultDTO::getSolvedAt).reversed())
                .toList();
    }
}
