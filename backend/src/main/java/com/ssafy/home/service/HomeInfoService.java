package com.ssafy.home.service;

import java.util.List;
import java.util.Map;

import com.ssafy.home.dto.HomeInfo;

public interface HomeInfoService {

//	List<HomeInfo> searchAptByNameUsingKMP(List<HomeInfo> allAptList, String keyword);
	
	// 페이징 기반 아파트 검색
	List<HomeInfo> searchAptList(String dongCode, String aptName, int page, int size);

	// 총 개수 조회
	int countAptList(String dongCode, String aptName);
	
	List<String> getSidoList();
	
	List<String> getGugunList(String sido);
	
	List<Map<String, String>> getDongList(String gugun);
}
