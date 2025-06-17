package com.quiz.quiz_app.controller;

import com.quiz.quiz_app.dto.UserDTO;
import com.quiz.quiz_app.entity.User;
import com.quiz.quiz_app.repository.UserRepository;
import com.quiz.quiz_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // ✅ 유저 생성 (일반 회원가입용)
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    // ✅ 로그인된 사용자 정보 조회 (소셜 로그인 성공 후)
    @GetMapping("/me/info")
    public ResponseEntity<UserDTO> getLoginUserInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return ResponseEntity.ok(UserDTO.from(user));
        }

        return ResponseEntity.status(401).build();
    }


    // ✅ 로그인 여부 확인용 (프론트 Navbar 표시용)
    @GetMapping("/me/check")
    public ResponseEntity<Map<String, Boolean>> checkLoginStatus() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAuthenticated = principal instanceof User;

        Map<String, Boolean> result = new HashMap<>();
        result.put("authenticated", isAuthenticated);

        return ResponseEntity.ok(result);
    }


    // ✅ 전체 유저 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ ID로 특정 유저 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}