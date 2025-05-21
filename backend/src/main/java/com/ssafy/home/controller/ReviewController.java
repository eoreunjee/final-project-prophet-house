package com.ssafy.home.controller;

import com.ssafy.home.dto.Comment;
import com.ssafy.home.dto.Review;
import com.ssafy.home.dto.ReviewEdit;
import com.ssafy.home.service.CommentService;
import com.ssafy.home.service.ReviewService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        reviewService.writeReview(review);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id,
                                          @RequestBody ReviewEdit dto
                                          ) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String loginId = authentication.getName();
    	System.out.println("🔐 로그인한 사용자 ID: " + loginId);
    	System.out.println("🧾 Authentication: " + authentication);

        Review review = reviewService.getReview(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }

        if (!review.getUserId().equals(loginId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
        }

        review.setLocation(dto.getLocation());
        review.setDealType(dto.getDealType());
        review.setContent(dto.getContent());

        reviewService.updateReview(review);

        return ResponseEntity.ok("수정 완료");
    }


    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> writeComment(@PathVariable Long id, @RequestBody Comment comment) {
        comment.setReviewId(id);
        commentService.writeComment(comment);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }
    
    @DeleteMapping("/{reviewId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long reviewId, @PathVariable Long commentId) {
        commentService.deleteComment(reviewId, commentId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/myReviews")
    public List<Review> getReviewsByUser(@RequestParam String userId) {
        return reviewService.getReviewsByUser(userId);
    }
    
    @GetMapping("/myComments")
    public List<Comment> getMyComments(@RequestParam String userId) {
        return commentService.getCommentsByUser(userId);
    }


}
