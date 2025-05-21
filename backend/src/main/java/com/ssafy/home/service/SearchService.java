package com.ssafy.home.service;

import java.util.List;
import java.util.Map;

import com.ssafy.home.dto.HomeDeal;
import com.ssafy.home.dto.HomeInfo;

public interface SearchService {

	List<HomeInfo> searchAptsByName(String aptName);
	
	Map<String, List<HomeDeal>> searchAptsByRegion(List<HomeInfo> aptList);
}
