package com.ssafy.home.mapper;


import org.apache.ibatis.annotations.Mapper;
import com.ssafy.home.dto.User;

@Mapper
public interface UserMapper {
    int createUser(User u); // 회원가입
    User selectUser(String id); // 본인 정보 조회
    User loginUser(String id); // 로그인
}
