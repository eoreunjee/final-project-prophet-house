package com.ssafy.home.controller;

import com.ssafy.home.dto.*;
import com.ssafy.home.service.AuthService;
import com.ssafy.home.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthService authService;

    // 회원 등록
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok().build();
    }
    
    // 회원 수정
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody User user) {
    	userService.updateUser(user);
    	return ResponseEntity.ok().build();
    }
    
    // 회원 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    
    // 회원 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.loginMember(loginRequest.getId(), loginRequest.getPassword());

        if (token != null) {
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }
    
    // 회원 정보 조회
    @GetMapping("/me")
    public User getUserInfo(Authentication authentication) {
    	String id = (String)authentication.getPrincipal(); // JWT에서 추출된 ID
        return userService.selectUser(id);
    }
    
}
