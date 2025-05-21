package com.ssafy.home.mapper;

import com.ssafy.home.dto.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);
    void deleteComment(Long commentId);
    List<Comment> getCommentsByReviewId(Long reviewId);
    List<Comment> selectCommentsByUser(String userId);
}
