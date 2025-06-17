// ✅ AnswerSubmitDTO.java
package com.quiz.quiz_app.dto;

import lombok.Getter;

@Getter
public class AnswerSubmitDTO {
    private Long quizId;
    private String userAnswer; // 🔄 문자열 입력값으로 수정
}