package com.ssafy.home.service;

import com.ssafy.home.dto.User;
import com.ssafy.home.mapper.UserMapper;
import com.ssafy.home.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // ✅ JwtUtil 주입

    @Override
    public String loginMember(String id, String rawPw) {
        User user = userMapper.loginUser(id);

        if (user != null && passwordEncoder.matches(rawPw, user.getPassword())) {
            return jwtUtil.generateToken(user.getId(), user.getName()); // ✅ 토큰 반환
        }
        return null;
    }

}
