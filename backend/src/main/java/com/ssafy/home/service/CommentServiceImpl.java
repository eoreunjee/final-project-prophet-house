package com.ssafy.home.service;

import com.ssafy.home.dto.Comment;
import com.ssafy.home.mapper.CommentMapper;
import com.ssafy.home.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public void writeComment(Comment comment) {
        commentMapper.insertComment(comment);
        reviewMapper.incrementCommentCount(comment.getReviewId()); // ✅ 댓글 수 +1
    }
    
    public void deleteComment(Long reviewId, Long commentId) {
        commentMapper.deleteComment(commentId);
        reviewMapper.decrementCommentCount(reviewId);
    }

    @Override
    public List<Comment> getComments(Long reviewId) {
        return commentMapper.getCommentsByReviewId(reviewId);
    }
    
    @Override
    public List<Comment> getCommentsByUser(String userId) {
        return commentMapper.selectCommentsByUser(userId);
    }

}
