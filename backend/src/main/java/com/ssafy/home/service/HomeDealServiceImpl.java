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
    
    private final HomeDealMapper homeDealDAO;
    
    public List<HomeDeal> getDealsByHouse(String aptSeq, boolean asc) {
        List<HomeDeal> deals = homeDealDAO.getDealsByHouse(aptSeq);
        int maxPrice = 0;
        for (HomeDeal deal : deals) {
            try {
                String dealAmount = deal.getDealAmount();
                System.out.println(dealAmount);
                if (dealAmount == null) continue;

                int amount = Integer.parseInt(dealAmount.replaceAll(",", "").trim());
                if (amount > maxPrice) maxPrice = amount;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        System.out.println("최대금액: " + maxPrice);

        List<List<HomeDeal>> count = new ArrayList<>();
        for (int i = 0; i <= maxPrice; i++) {
            count.add(new ArrayList<>());
        }

        for (HomeDeal deal : deals) {
            try {
                String dealAmount = deal.getDealAmount();
                if (dealAmount == null) continue;

                int amount = Integer.parseInt(dealAmount.replaceAll(",", "").trim());
                count.get(amount).add(deal);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        List<HomeDeal> sorted = new ArrayList<>();
        if (asc) {
            for (int i = 0; i <= maxPrice; i++) {
                sorted.addAll(count.get(i));
            }
        } else {
            for (int i = maxPrice; i >= 0; i--) {
                sorted.addAll(count.get(i));
            }
        }

        return sorted;
    }
}