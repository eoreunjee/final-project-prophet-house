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
            @RequestParam String sido,
            @RequestParam String gugun,
            @RequestParam String dong,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String aptName
    ) {
        List<HomeInfo> aptList = homeInfoService.getHousesByDong(sido, gugun, dong);

        if (aptName != null && !aptName.isBlank()) {
            aptList = homeInfoService.searchAptByNameUsingKMP(aptList, aptName);
        }

        boolean ascending = "asc".equalsIgnoreCase(sort);
        Map<String, List<HomeDeal>> dealMap = new HashMap<>();
        for (HomeInfo apt : aptList) {
            List<HomeDeal> deals = homeDealService.getDealsByHouse(apt.getAptSeq(), ascending);
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
