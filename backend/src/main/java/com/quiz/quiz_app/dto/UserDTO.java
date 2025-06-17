package com.quiz.quiz_app.dto;

import com.quiz.quiz_app.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDTO {
    // 유저 생성과 조회에 동일하게 사용
    private Long userId; // 유저 ID
    private String username; // 닉네임
    private int score; // 점수
    private LocalDateTime createdAt;

    // UserDTO 값을 Entity로 변환 후 User DB에 저장하기 위한 작업
    public User toEntity() {
        return  User.builder()
                .username(username)
                .score(score)
                .build();
    }

    // User라는 Entity에서 DTO로 정보를 변환 후 가져오는 작업
    public static UserDTO from(User user){
        return UserDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .score(user.getScore())
                .createdAt(user.getCreatedAt())
                .build();
    }

}