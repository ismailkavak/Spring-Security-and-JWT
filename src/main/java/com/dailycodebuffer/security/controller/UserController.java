package com.dailycodebuffer.security.controller;

import com.dailycodebuffer.security.entity.User;
import com.dailycodebuffer.security.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userRepository.save(user);
    }
}
