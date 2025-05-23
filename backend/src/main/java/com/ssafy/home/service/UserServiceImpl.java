package com.ssafy.home.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.home.dto.User;
import com.ssafy.home.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public int createUser(User user) {
        String rawPw = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPw));  // ✅ BCrypt로 암호화
        return userMapper.createUser(user);
    }

	@Override
	public User selectUser(String id) {
		return userMapper.selectUser(id);
	}

	@Override
	public void updateUser(User user) {
		if (user.getPassword() != null && !user.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		} else {
			userMapper.updateUser(null);
		}
		userMapper.updateUser(user);
	}

}
