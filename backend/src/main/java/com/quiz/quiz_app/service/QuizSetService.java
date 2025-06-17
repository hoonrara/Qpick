package com.quiz.quiz_app.service;

import com.quiz.quiz_app.dto.QuizSetDTO;
import com.quiz.quiz_app.entity.QuizSet;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.QuizSetRepository;
import com.quiz.quiz_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuizSetService {

    private final QuizSetRepository quizSetRepository;
    private final UserRepository userRepository;

    // ✅ 검색 기능 포함된 전체 퀴즈셋 목록 조회
    public List<QuizSetDTO> getQuizSets(String search) {
        List<QuizSet> quizSets;

        if (search != null && !search.isBlank()) {
            quizSets = quizSetRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);
        } else {
            quizSets = quizSetRepository.findAll();
        }

        return quizSets.stream().map(QuizSetDTO::from).collect(Collectors.toList());
    }

    // ✅ 유저가 만든 퀴즈셋 목록 조회
    public List<QuizSetDTO> getQuizSetsByUser(Long userId) {
        return quizSetRepository.findByCreatorId(userId).stream()
                .map(QuizSetDTO::from)
                .collect(Collectors.toList());
    }

    // ✅ 퀴즈셋 생성
    @Transactional
    public QuizSetDTO createQuizSet(String title, String description, String imageUrl, Long userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        QuizSet set = QuizSet.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .creator(creator)
                .build();

        QuizSet saved = quizSetRepository.save(set);
        return QuizSetDTO.from(saved);
    }

    // ✅ 퀴즈셋 단일 조회
    public QuizSetDTO getQuizSetById(Long id) {
        QuizSet quizSet = quizSetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈셋을 찾을 수 없습니다"));
        return QuizSetDTO.from(quizSet);
    }

    // ✅ 퀴즈셋 삭제
    @Transactional
    public void deleteQuizSet(Long quizSetId, Long userId) {
        QuizSet quizSet = quizSetRepository.findById(quizSetId)
                .orElseThrow(() -> new IllegalArgumentException("퀴즈셋이 존재하지 않습니다."));

        if (!quizSet.getCreator().getId().equals(userId)) {
            throw new SecurityException("본인이 만든 퀴즈셋만 삭제할 수 있습니다.");
        }

        quizSetRepository.delete(quizSet);
    }
}
