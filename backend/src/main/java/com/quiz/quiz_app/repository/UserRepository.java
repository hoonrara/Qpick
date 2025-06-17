package com.quiz.quiz_app.repository;

import com.quiz.quiz_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialId(String socialId);

    /**
     * 닉네임(username)으로 유저를 찾는 메서드 (중복 체크용 등)
     */
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
