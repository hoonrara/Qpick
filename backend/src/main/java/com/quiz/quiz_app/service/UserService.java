package com.quiz.quiz_app.service;

import com.quiz.quiz_app.dto.UserDTO;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * [유저 생성]
     * - 닉네임을 기반으로 유저 객체 생성
     * - 점수 초기값은 0
     */
    @Transactional
    public UserDTO createUser(UserDTO dto) {
        User user = dto.toEntity();            // DTO → Entity 변환
        User saved = userRepository.save(user); // DB에 저장
        return UserDTO.from(saved);            // 저장된 Entity → DTO로 반환
    }

    /**
     * [전체 유저 조회]
     * - 모든 유저 리스트를 조회하고 DTO로 변환
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * [특정 유저 조회]
     * - ID로 유저 하나 조회
     * - 없으면 예외 발생
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + id));
        return UserDTO.from(user);
    }
}
