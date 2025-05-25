package com.ssafy.home.controller;

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
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String gugun,
            @RequestParam(required = false) String dong,
            @RequestParam(required = false) String aptName
    ) {
        List<HomeInfo> aptList;

        // 1. 지역 조건이 없고 아파트 이름만 있는 경우 → 전체 검색
        if ((sido == null || sido.isBlank()) &&
            (gugun == null || gugun.isBlank()) &&
            (dong == null || dong.isBlank()) &&
            aptName != null && !aptName.isBlank()) {
            aptList = homeInfoService.searchAptByNameAll(aptName); // 새로 만들어야 할 메서드
        } else {
            // 2. 지역 조건 기반 필터
            aptList = homeInfoService.getHousesByDong(sido, gugun, dong);

            // 3. 이름 검색이 추가로 있는 경우 → KMP 필터링
            if (aptName != null && !aptName.isBlank()) {
                aptList = homeInfoService.searchAptByNameUsingKMP(aptList, aptName);
            }
        }

        // 4. dealMap 구성 (기본 정렬은 최신순)
        Map<String, List<HomeDeal>> dealMap = new HashMap<>();
        for (HomeInfo apt : aptList) {
            List<HomeDeal> deals = homeDealService.getDealsByHouse(apt.getAptSeq(), false); // 최신순
            dealMap.put(apt.getAptSeq(), deals);
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
    public List<String> getDongList(@RequestParam("gugunName") String gugun) {
        return homeInfoService.getDongList(gugun);
    }
}
