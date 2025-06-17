package com.quiz.quiz_app.controller;

import com.quiz.quiz_app.dto.AnswerDTO;
import com.quiz.quiz_app.dto.AnswerResultDTO;
import com.quiz.quiz_app.dto.AnswerSubmitDTO;
import com.quiz.quiz_app.dto.QuizSetHistoryResultDTO;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.UserRepository;
import com.quiz.quiz_app.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final UserRepository userRepository;

    // ✅ 유저 정답 제출 & 채점 (비로그인도 허용)
    @PostMapping("/submit")
    public ResponseEntity<AnswerResultDTO> submitAnswers(
            @RequestBody List<AnswerSubmitDTO> answers,
            @RequestParam(required = false, defaultValue = "0") Long userId) {
        // userId가 0이면 로그인 안 한 사용자로 간주
        AnswerResultDTO result = answerService.submitAnswers(userId, answers);
        return ResponseEntity.ok(result);
    }

    // ✅ 현재 로그인한 사용자의 풀이 기록 조회
    @GetMapping("/user/me")
    public ResponseEntity<List<AnswerDTO>> getMyAnswers() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            List<AnswerDTO> answers = answerService.getAnswersByUser(user.getId());
            return ResponseEntity.ok(answers);
        }

        return ResponseEntity.status(401).build();
    }


    // ✅ 특정 퀴즈의 정답 기록 조회
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<AnswerDTO>> getAnswersByQuiz(@PathVariable Long quizId) {
        List<AnswerDTO> answers = answerService.getAnswersByQuiz(quizId);
        return ResponseEntity.ok(answers);
    }


    @GetMapping("/history/quizsets/{userId}")
    public ResponseEntity<List<QuizSetHistoryResultDTO>> getQuizSetHistories(@PathVariable Long userId) {
        return ResponseEntity.ok(answerService.getQuizSetHistory(userId));
    }

}
