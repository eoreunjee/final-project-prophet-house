package com.ssafy.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class HomeDeal {
    private int no;
    private String aptSeq;
    private String aptDong;
    private int floor;
    private int dealYear;
    private int dealMonth;
    private int dealDay;
    private String dealAmount;
}