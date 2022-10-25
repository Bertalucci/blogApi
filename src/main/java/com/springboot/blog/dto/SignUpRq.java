package com.springboot.blog.dto;

import lombok.Data;

@Data
public class SignUpRq {
    String name;
    String username;
    String email;
    String password;
}
