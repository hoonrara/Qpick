package com.quiz.quiz_app.controller;

import com.quiz.quiz_app.dto.QuizRequestDTO;
import com.quiz.quiz_app.dto.QuizResponseDTO;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    /**
     * ✅ 퀴즈 일괄 등록 (10개)
     */
    @PostMapping("/bulk/{quizSetId}")
    public ResponseEntity<Void> createQuizBulk(
            @PathVariable Long quizSetId,
            @RequestBody List<QuizRequestDTO> requestList,
            @AuthenticationPrincipal User user
    ) {
        quizService.createQuizBatch(quizSetId, requestList, user);
        return ResponseEntity.ok().build();
    }


    // 퀴즈셋 클릭 시, 해당 퀴즈셋의 전체 문제 가져오기
    @GetMapping("/set/{quizSetId}")
    public ResponseEntity<List<QuizResponseDTO>> getQuizzesBySet(@PathVariable Long quizSetId) {
        return ResponseEntity.ok(quizService.getQuizzesBySet(quizSetId));
    }

    // 전체 퀴즈 목록 조회 (관리자용)
    @GetMapping("/all")
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }
}
