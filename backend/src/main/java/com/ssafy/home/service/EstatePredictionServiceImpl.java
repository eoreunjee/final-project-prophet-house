package com.ssafy.home.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ssafy.home.dto.EstatePredictionRequest;
import com.ssafy.home.dto.EstatePredictionResonse;

@Service
public class EstatePredictionServiceImpl implements EstatePredictionService{
	
	private final RestTemplate restTemplate;

    public EstatePredictionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public EstatePredictionResonse predict(EstatePredictionRequest request) {
        String fastApiUrl = "http://192.168.205.75:8000/predict";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EstatePredictionRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<EstatePredictionResonse> response = restTemplate.exchange(
                fastApiUrl,
                HttpMethod.POST,
                entity,
                EstatePredictionResonse.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("FastAPI 예측 서버 호출 실패: " + e.getMessage());
        }
    }

}
