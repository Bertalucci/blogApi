package com.springboot.blog.service;

import com.springboot.blog.dto.PostRq;
import com.springboot.blog.dto.PostRs;


public interface PostService {
    PostRq createPost(PostRq postRq);
    PostRs getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostRq getPostById(Long id);
    PostRq updatePost(PostRq postRq, Long id);
    void deletePostById(Long id);
}
