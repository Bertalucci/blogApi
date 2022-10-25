package com.springboot.blog.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CommentRq {
    private Long id;

    @NotEmpty(message = "Name should not be null or empty")
    @Size
    private String name;

    @NotEmpty(message = "Email should not be null or empty")
    @Email
    private String email;

    @NotEmpty(message = "Comment body should not be null or empty")
    @Size(min = 10, message = "Comment body should have at least 10 characters")
    private String body;
}
