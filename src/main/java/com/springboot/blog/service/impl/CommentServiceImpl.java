package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentRq;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentRq createComment(Long postId, CommentRq commentRq) {
        Comment comment = mapToEntity(commentRq);

        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentRq> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentRq getCommentById(Long postId, Long commentId) {
        return mapToDTO(getCommentEntityById(postId, commentId));
    }

    @Override
    public CommentRq updateComment(Long postId, Long commentId, CommentRq commentRq) {
        Comment comment = getCommentEntityById(postId, commentId);
        comment.setName(commentRq.getName());
        comment.setEmail(commentRq.getEmail());
        comment.setBody(commentRq.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        commentRepository.delete(getCommentEntityById(postId, commentId));
    }

    /**
     * Получает Comment из БД по id, предварительно проверив, что существует Post.
     */
    private Comment getCommentEntityById(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to the post");
        }
        return comment;
    }

    /**
     * Конвертит entity to DTO
     */
    private CommentRq mapToDTO(Comment comment) {
        return modelMapper.map(comment, CommentRq.class);
    }

    /**
     * Конвертит DTO to entity
     */
    private Comment mapToEntity(CommentRq commentRq) {
        return modelMapper.map(commentRq, Comment.class);
    }
}
