package com.ssafy.home.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SearchCondition {
    private int currentPage;
    private int itemsPerPage = 10;
    private String sort = "id";   // 정렬 기준 필드
    private String order = "asc"; // 정렬 방향

    public SearchCondition(int currentPage) {
        this.currentPage = currentPage;
    }

    public SearchCondition(int currentPage, String sort, String order) {
        this.currentPage = currentPage;
        this.sort = sort != null ? sort : "id";
        this.order = order != null ? order : "asc";
    }

    public int getOffset() {
        return (currentPage - 1) * itemsPerPage;
    }

}
