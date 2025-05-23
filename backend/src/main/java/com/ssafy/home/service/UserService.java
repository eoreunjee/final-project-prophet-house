package com.ssafy.home.service;

import com.ssafy.home.dto.User;

public interface UserService {

	int createUser(User memberdto);
	
	void updateUser(User user);
	
	void deleteUser(String id);
	
	User selectUser(String id);
}
