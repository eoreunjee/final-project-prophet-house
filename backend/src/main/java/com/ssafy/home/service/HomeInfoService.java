package com.ssafy.home.service;

import java.util.List;
import java.util.Map;

import com.ssafy.home.dto.HomeInfo;

public interface HomeInfoService {

	List<HomeInfo> searchAptByNameUsingKMP(List<HomeInfo> allAptList, String keyword);
	
	List<HomeInfo> searchAptByNameAll(String aptName);
	
	public List<HomeInfo> getHousesByDong(String dong);
	
	List<String> getSidoList();
	
	List<String> getGugunList(String sido);
	
	List<Map<String, String>> getDongList(String gugun);
}
