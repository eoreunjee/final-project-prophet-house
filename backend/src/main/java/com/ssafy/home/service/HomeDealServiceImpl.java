package com.ssafy.home.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.dto.HomeDeal;
import com.ssafy.home.mapper.HomeDealMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeDealServiceImpl implements HomeDealService {
    
    private final HomeDealMapper homeDealMapper;
    
    @Override
    public List<HomeDeal> getDealsByHouse(String aptSeq) {
        return homeDealMapper.getDealsByHouse(aptSeq);
    }

}