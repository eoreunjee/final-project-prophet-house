package com.ssafy.home.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.home.dto.HomeDeal;
import com.ssafy.home.dto.HomeInfo;
import com.ssafy.home.mapper.SearchMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	
	private final SearchMapper searchMapper;
	
	@Override
	public List<HomeInfo> searchAptsByName(String aptName) {
	    return searchMapper.searchAptsByName(aptName);
	}

	@Override
	public Map<String, List<HomeDeal>> searchAptsByRegion(List<HomeInfo> aptList) {
	    List<String> aptSeqList = aptList.stream()
	                                     .map(HomeInfo::getAptSeq)
	                                     .toList();

	    List<HomeDeal> deals = searchMapper.selectDealsByAptSeq(aptSeqList);

	    // aptSeq 기준으로 groupBy
	    return deals.stream().collect(Collectors.groupingBy(HomeDeal::getAptSeq));
	}
	
	@Override
    public String getRegionDongName(String sido, String gugun, String dong) {
        String dongCode = searchMapper.findDongCode(sido, gugun, dong);
        if (dongCode == null) return null;
        
        // 소수점 제거
        if (dongCode.contains(".")) {
            dongCode = dongCode.substring(0, dongCode.indexOf('.'));
        }

        // 10자리라면 앞 5자리만 사용
        if (dongCode.length() >= 5) {
            dongCode = dongCode.substring(0, 5);
        }

        String regionDongName = dongCode + "_" + dong;
        return regionDongName;
    }
}
