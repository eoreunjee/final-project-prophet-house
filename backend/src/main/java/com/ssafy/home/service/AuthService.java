package com.ssafy.home.service;

import com.ssafy.home.dto.User;

public interface AuthService {
	
	String loginMember(String id, String pw);
	
}