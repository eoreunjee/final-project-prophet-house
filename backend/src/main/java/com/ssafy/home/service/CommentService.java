package com.ssafy.home.service;

import com.ssafy.home.dto.Comment;
import java.util.List;

public interface CommentService {
    void writeComment(Comment comment);
    void deleteComment(Long reviewId, Long commentId);
    List<Comment> getComments(Long reviewId);
    List<Comment> getCommentsByUser(String userId);
}
