package com.ssafy.home.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.home.dto.HomeInfo;
import com.ssafy.home.mapper.HomeInfoMapper;
import com.ssafy.home.util.KMPUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeInfoServiceImpl implements HomeInfoService {
	private final HomeInfoMapper homeInfoMapper;
    
    public List<HomeInfo> searchAptByNameUsingKMP(List<HomeInfo> allAptList, String keyword) {
        List<HomeInfo> result = new ArrayList<>();

        for (HomeInfo apt : allAptList) {
            if (KMPUtil.kmpContains(apt.getAptName(), keyword)) {
                result.add(apt);
            }
        }
        return result;
    }
    
    public List<HomeInfo> getHousesByDong(String sido, String gugun, String dong) {
        return homeInfoMapper.getHousesByDong(sido, gugun, dong);
    }
    
    public List<String> getSidoList() {
    	return homeInfoMapper.getSidoList();
    }
    public List<String> getGugunList(String sido) {
    	return homeInfoMapper.getGugunList(sido);
    }

    public List<String> getDongList(String gugun) {
    	return homeInfoMapper.getDongList(gugun);
    }

}