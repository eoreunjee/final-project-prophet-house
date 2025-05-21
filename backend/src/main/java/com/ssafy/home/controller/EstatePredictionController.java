package com.ssafy.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.home.dto.EstatePredictionRequest;
import com.ssafy.home.dto.EstatePredictionResonse;
import com.ssafy.home.service.EstatePredictionService;



@RestController
@RequestMapping("/api/predict")
public class EstatePredictionController {
	private final EstatePredictionService predictService;

    public EstatePredictionController(EstatePredictionService predictService) {
        this.predictService = predictService;
    }

    @PostMapping
    public ResponseEntity<EstatePredictionResonse> predict(@RequestBody EstatePredictionRequest request) {
    	EstatePredictionResonse response = predictService.predict(request);
        return ResponseEntity.ok(response);
    }

}
