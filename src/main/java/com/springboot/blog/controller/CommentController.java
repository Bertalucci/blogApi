package com.springboot.blog.controller;

import com.springboot.blog.dto.CommentRq;
import com.springboot.blog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(value = "CRUD REST API for Comment resources")
@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Create Comment REST API")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentRq> createComment(@PathVariable(name = "postId") Long id,
                                                   @Valid @RequestBody CommentRq commentRq) {
        return new ResponseEntity<>(commentService.createComment(id, commentRq), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create Comment By Post id REST API")
    @GetMapping("/posts/{postId}/comments")
    public List<CommentRq> getCommentsByPostId(@PathVariable(value = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }


    @ApiOperation(value = "Get Comment By id REST API")
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRq> getCommentById(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "id") Long commentId) {
        CommentRq commentRq = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(commentRq, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Comment REST API")
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentRq> updateComment(@PathVariable(value = "postId") Long postId,
                                                   @PathVariable(value = "id") Long commentId,
                                                   @Valid @RequestBody CommentRq commentRq) {
        CommentRq updatedComment = commentService.updateComment(postId, commentId, commentRq);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Comment REST API")
    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "id") Long commentId) {
        commentService.deleteCommentById(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

}
