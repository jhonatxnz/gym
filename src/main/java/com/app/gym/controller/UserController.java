package com.app.gym.controller;

import com.app.gym.dto.CreateUserRequest;
import com.app.gym.model.User;
import com.app.gym.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User newUser = userService.createUser(createUserRequest);
        return ResponseEntity.ok(newUser);
    }

}