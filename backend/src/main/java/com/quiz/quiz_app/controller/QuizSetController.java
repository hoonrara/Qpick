package com.quiz.quiz_app.controller;

import com.quiz.quiz_app.dto.QuizSetDTO;
import com.quiz.quiz_app.service.QuizSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizsets")
@RequiredArgsConstructor
public class QuizSetController {

    private final QuizSetService quizSetService;

    // ✅ 검색 기능 포함된 전체 퀴즈셋 목록 조회
    @GetMapping
    public ResponseEntity<List<QuizSetDTO>> getQuizSets(
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(quizSetService.getQuizSets(search));
    }

    // ✅ 퀴즈셋 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<QuizSetDTO> getQuizSetById(@PathVariable Long id) {
        return ResponseEntity.ok(quizSetService.getQuizSetById(id));
    }

    // ✅ 퀴즈셋 생성
    @PostMapping
    public ResponseEntity<QuizSetDTO> createQuizSet(@RequestBody QuizSetDTO dto) {
        QuizSetDTO created = quizSetService.createQuizSet(
                dto.getTitle(),
                dto.getDescription(),
                dto.getImageUrl(),
                dto.getUserId()
        );
        return ResponseEntity.ok(created);
    }

    // ✅ 특정 유저가 만든 퀴즈셋 조회
    @GetMapping("/created/{userId}")
    public ResponseEntity<List<QuizSetDTO>> getQuizSetsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(quizSetService.getQuizSetsByUser(userId));
    }

    // ✅ 내가 만든 퀴즈셋 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuizSet(@PathVariable Long id, @RequestParam Long userId) {
        quizSetService.deleteQuizSet(id, userId);
        return ResponseEntity.noContent().build();
    }
}
