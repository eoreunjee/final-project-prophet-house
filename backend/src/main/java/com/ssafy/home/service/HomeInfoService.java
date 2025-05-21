package com.ssafy.home.service;

import java.util.List;

import com.ssafy.home.dto.HomeInfo;

public interface HomeInfoService {

	List<HomeInfo> searchAptByNameUsingKMP(List<HomeInfo> allAptList, String keyword);
	
	List<HomeInfo> getHousesByDong(String sido, String gugun, String dong);
	
	List<String> getSidoList();
	
	List<String> getGugunList(String sido);
	
	List<String> getDongList(String gugun);
}
