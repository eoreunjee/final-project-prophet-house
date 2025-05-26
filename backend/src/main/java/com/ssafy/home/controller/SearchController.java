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
import com.ssafy.home.service.SearchService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@CrossOrigin(origins = "*")
public class SearchController {

    private final HomeInfoService homeInfoService;
    private final HomeDealService homeDealService;
    private final SearchService searchService;

    @GetMapping("/apt")
    public ResponseEntity<?> searchAptList(
            @RequestParam(required = false) String dongCode,
            @RequestParam(required = false) String aptName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<HomeInfo> aptList = homeInfoService.searchAptList(dongCode, aptName, page, size);
        int total = homeInfoService.countAptList(dongCode, aptName);

        Map<String, Object> result = Map.of(
                "aptList", aptList,
                "totalCount", total
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/deals")
    public ResponseEntity<?> getDealsByAptSeq(@RequestParam String aptSeq) {
        List<HomeDeal> deals = homeDealService.getDealsByHouse(aptSeq);
        return ResponseEntity.ok(deals);
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
    
    // regionDongName 반환 엔드포인트 추가
    @GetMapping("/region-dong-name")
    public ResponseEntity<String> getRegionDongName(
            @RequestParam String sido,
            @RequestParam String gugun,
            @RequestParam String dong) {

        String regionDongName = searchService.getRegionDongName(sido, gugun, dong);
        if (regionDongName == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(regionDongName);
    }
    
}
