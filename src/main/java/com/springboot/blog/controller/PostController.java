package com.springboot.blog.controller;

import com.springboot.blog.dto.PostRq;
import com.springboot.blog.dto.PostRs;
import com.springboot.blog.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.springboot.blog.utils.AppConstants.*;

@Api(value = "CRUD REST API for Post resources")
@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @ApiOperation(value = "Create Post REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PostRq> createPost(@Valid @RequestBody PostRq postRq) {
        return new ResponseEntity<>(postService.createPost(postRq), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get All Posts REST API")
    @GetMapping
    public PostRs getAllPosts(@RequestParam (value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                              @RequestParam (value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE, required = false) int pageSize,
                              @RequestParam (value = "sortBy", defaultValue = DEFAULT_SORT_BY, required = false) String sortBy,
                              @RequestParam (value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION, required = false) String sortDir) {

        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }


    @ApiOperation(value = "Get Post By Id REST API")
    @GetMapping("/{id}")
    public ResponseEntity<PostRq> getPostById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @ApiOperation(value = "Update Post REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostRq> updatePost(@Valid @RequestBody PostRq postRq, @PathVariable(name = "id") Long id) {
        PostRq postResponse = postService.updatePost(postRq, id);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Post REST API")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long id) {
        postService.deletePostById(id);
        return new ResponseEntity<>("Post entity deleted successfully", HttpStatus.OK);
    }
}
