package com.ssafy.home.service;

import java.util.List;

import com.ssafy.home.dto.HomeDeal;

public interface HomeDealService {

	List<HomeDeal> getDealsByHouse(String aptSeq);
}
