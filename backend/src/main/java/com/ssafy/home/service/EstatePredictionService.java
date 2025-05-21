package com.ssafy.home.service;

import com.ssafy.home.dto.EstatePredictionRequest;
import com.ssafy.home.dto.EstatePredictionResonse;

public interface EstatePredictionService {
	EstatePredictionResonse predict(EstatePredictionRequest request);
}
