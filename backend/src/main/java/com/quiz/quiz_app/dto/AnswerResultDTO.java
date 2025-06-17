package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class AnswerResultDTO {
    // 유저가 푼 전체 10문제의 최종 결과
    private int score; // 점수(맞춘 개수 x 10 예정)
    private int correctCount; // 맞춘 개수
    private List<AnswerDTO> answers; // AnswerDTO 리스트

    // Answer라는 Entity에서 DTO로 정보를 변환 후 가져오는 작업
    public static AnswerResultDTO from(List<Answer> answerList) {
        // 기본적으로 결과는 Long형이기에 int형으로 변환
        // answerList를 스트림으로 변환
        // Answer에서 isCorrect(정답을 맞춘 것)을 필터링 후 개수 파악
        int correctCount = (int) answerList.stream().filter(Answer::isCorrect).count();

        // answerList를 스트림으로 변환
        // answer 객체를 AnswerDTO로 변환
        // toList()를 이용해서 리스트로 변환해서 저장
        List<AnswerDTO> answerDTO = answerList.stream().map(AnswerDTO::fromEntity).toList();

        // AnswerResultDTO에 있는 객체를 score, correctCount, answerDTO라는 변수로 리턴
        return  new AnswerResultDTO(correctCount * 10, correctCount, answerDTO);
    }

}
