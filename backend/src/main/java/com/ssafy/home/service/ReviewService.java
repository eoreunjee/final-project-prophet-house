package com.ssafy.home.service;

import com.ssafy.home.dto.Review;

import java.util.List;

public interface ReviewService {
    void writeReview(Review review);
    List<Review> getAllReviews();
    Review getReview(Long id);
    void updateReview(Review review);
    void deleteReview(Long id);
    List<Review> getReviewsByUser(String userId);
}
