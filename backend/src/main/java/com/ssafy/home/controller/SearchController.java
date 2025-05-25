package com.ssafy.home.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssafy.home.dto.HomeDeal;
import com.ssafy.home.dto.HomeInfo;
import com.ssafy.home.service.HomeDealService;
import com.ssafy.home.service.HomeInfoService;
import com.ssafy.home.service.HomeDealServiceImpl;
import com.ssafy.home.service.HomeInfoServiceImpl;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final HomeInfoService homeInfoService;
    private final HomeDealService homeDealService;

    public SearchController(HomeInfoServiceImpl homeInfoService, HomeDealServiceImpl homeDealService) {
        this.homeInfoService = homeInfoService;
        this.homeDealService = homeDealService;
    }

    @GetMapping("/apt")
    public Map<String, Object> searchApartment(
            @RequestParam(required = false) String dongCode,
            @RequestParam(required = false) String aptName
    ) {
    	System.out.println("🟦 [SearchController] 받은 dongCode = " + dongCode);
        System.out.println("🟦 [SearchController] 받은 aptName  = " + aptName);
        List<HomeInfo> aptList;

        // 1. dongCode 없고 aptName만 있는 경우 → 전체 이름 기반 검색
        if ((dongCode == null || dongCode.isBlank()) && aptName != null && !aptName.isBlank()) {
            aptList = homeInfoService.searchAptByNameAll(aptName);
        } else if (dongCode != null && !dongCode.isBlank()) {
            // 2. dongCode 있는 경우
            aptList = homeInfoService.getHousesByDong(dongCode);

            // 3. 추가로 이름도 입력된 경우 → KMP 필터
            if (aptName != null && !aptName.isBlank()) {
                aptList = homeInfoService.searchAptByNameUsingKMP(aptList, aptName);
            }
        } else {
            aptList = new ArrayList<>();
        }

        // 거래 정보 매핑
        Map<String, List<HomeDeal>> dealMap = new HashMap<>();
        for (HomeInfo apt : aptList) {
            if (apt.getAptSeq() != null) {
                dealMap.put(apt.getAptSeq(), homeDealService.getDealsByHouse(apt.getAptSeq(), false));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("aptList", aptList);
        result.put("dealMap", dealMap);
        return result;
    }



    @GetMapping("/sido")
    public List<String> getSidoList() {
        return homeInfoService.getSidoList();
    }

    // 구군 리스트
    @GetMapping("/gugun")
    public List<String> getGugunList(@RequestParam("sidoName") String sido) {
        return homeInfoService.getGugunList(sido);
    }

    // 동 리스트
    @GetMapping("/dong")
    public List<Map<String, String>> getDongList(@RequestParam("gugunName") String gugun) {
        return homeInfoService.getDongList(gugun);
    }
}
