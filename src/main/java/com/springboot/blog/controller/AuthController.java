package com.springboot.blog.controller;

import com.springboot.blog.dto.JwtAuthRs;
import com.springboot.blog.dto.LoginRq;
import com.springboot.blog.dto.SignUpRq;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Api(value = "Auth controller exposes sign in and sign up rest api")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @ApiOperation(value = "Rest api to sign in user to blog app")
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthRs> authenticateUser(@RequestBody LoginRq loginRq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRq.getUsernameOrEmail(), loginRq.getPassword()) );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //get token from tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthRs(token));
    }

    @ApiOperation(value = "Rest api to register user to blog app")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRq signUpRq) {
        //check if username exists in DB
        if (userRepository.existsByUsername(signUpRq.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        //check if email exists in DB
        if (userRepository.existsByEmail(signUpRq.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        //create user object
        User user = new User();
        user.setName(signUpRq.getName());
        user.setUsername(signUpRq.getUsername());
        user.setEmail(signUpRq.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRq.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRoles(Collections.singleton(roles));

        //save user object in DB
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
