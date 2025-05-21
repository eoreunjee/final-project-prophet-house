package com.ssafy.home.mapper;

import com.ssafy.home.dto.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    void insertReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long reviewId);
    void deleteReview(Long reviewId);
    void incrementCommentCount(Long reviewId);
    void decrementCommentCount(Long reviewId);
    void updateReview(Review review);
    List<Review> selectReviewsByUser(String userId);

}
