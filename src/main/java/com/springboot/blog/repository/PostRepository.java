package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

//можно не добавлять аннотацию @Repository, т.к. она наследуется от CrudRepository
public interface PostRepository extends JpaRepository<Post, Long> {

}
