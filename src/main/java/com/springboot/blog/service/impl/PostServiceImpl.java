package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.dto.PostRq;
import com.springboot.blog.dto.PostRs;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostRq createPost(PostRq postRq) {

        Post post = mapToEntity(postRq);

        //save to DB
        Post newPost = postRepository.save(post);

        return mapToDTO(newPost);
    }

    @Override
    public PostRs getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts = postRepository.findAll(pageable);

        //retrieve Post objects from Page
        List<Post> listOfPosts = posts.getContent();

        List<PostRq> content =  listOfPosts.stream().map(this::mapToDTO).collect(Collectors.toList());
        PostRs postRs = new PostRs();
        postRs.setContent(content);
        postRs.setPageNo(posts.getNumber());
        postRs.setPageSize(posts.getSize());
        postRs.setTotalElements(posts.getTotalElements());
        postRs.setTotalPages(posts.getTotalPages());
        postRs.setLast(posts.isLast());

        return postRs;
    }

    @Override
    public PostRq getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostRq updatePost(PostRq postRq, Long id) {
        //get post by id from db
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postRq.getTitle());
        post.setDescription(postRq.getDescription());
        post.setContent(postRq.getContent());

        //save updated post in db
        Post updatedPost = postRepository.save(post);

        //return dto
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    //convert DTO into entity
    private Post mapToEntity(PostRq postRq) {
        return modelMapper.map(postRq, Post.class);
    }

    //convert entity into DTO
    private PostRq mapToDTO(Post post) {
        return modelMapper.map(post, PostRq.class);
    }
}
