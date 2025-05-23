package com.ssafy.home.service;

import com.ssafy.home.dto.User;

public interface UserService {

	int createUser(User memberdto);
	
	void updateUser(User user);
	
	User selectUser(String id);
}
