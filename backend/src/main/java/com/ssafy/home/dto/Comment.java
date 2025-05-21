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
public class Comment {
    private Long commentId;
    private Long reviewId;
    private String userId;
    private String content;
    private Timestamp createdAt;
}
