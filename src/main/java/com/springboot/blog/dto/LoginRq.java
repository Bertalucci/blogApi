package com.springboot.blog.dto;

import lombok.Data;

@Data
public class LoginRq {
    private String usernameOrEmail;
    private String password;
}
