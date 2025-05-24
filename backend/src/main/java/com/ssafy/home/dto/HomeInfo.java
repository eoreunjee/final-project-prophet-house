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
public class HomeInfo {
    private String aptSeq;
    private String aptName;
    private String roadNm;
    private String roadNmBonbun;
    private String roadNmBubun;
    private int buildYear;
    private String latitude;
    private String longitude;
}