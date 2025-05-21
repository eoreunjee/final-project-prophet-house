package com.ssafy.home.service;

import com.ssafy.home.dto.Review;
import com.ssafy.home.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;

    @Override
    public void writeReview(Review review) {
        reviewMapper.insertReview(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewMapper.getAllReviews();
    }

    @Override
    public Review getReview(Long id) {
        return reviewMapper.getReviewById(id);
    }

    @Override
    public void deleteReview(Long id) {
        reviewMapper.deleteReview(id);
    }

	@Override
	public void updateReview(Review review) {
		reviewMapper.updateReview(review);
	}
	
	@Override
	public List<Review> getReviewsByUser(String userId) {
	    return reviewMapper.selectReviewsByUser(userId);
	}

}
