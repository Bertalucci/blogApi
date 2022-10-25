package com.springboot.blog.service;

import com.springboot.blog.dto.CommentRq;

import java.util.List;

public interface CommentService {
    CommentRq createComment(Long postId, CommentRq commentRq);
    List<CommentRq> getCommentsByPostId(Long postId);
    CommentRq getCommentById(Long postId, Long commentId);
    CommentRq updateComment(Long postId, Long commentId, CommentRq commentRq);
    void deleteCommentById(Long postId, Long commentId);
}
