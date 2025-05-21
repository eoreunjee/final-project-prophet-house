package com.ssafy.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class Review {
    private Long reviewId;
    private String userId;
    private String location;
    private String dealType;
    private String content;
    private int commentCount;
    private Timestamp createdAt;
}
