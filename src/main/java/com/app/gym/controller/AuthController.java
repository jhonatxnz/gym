package com.app.gym.controller;

import com.app.gym.dto.CreateUserRequestDto;
import com.app.gym.dto.CreateUserRequestDto;
import com.app.gym.dto.LoginRequestDto;
import com.app.gym.dto.LoginRequestDto;
import com.app.gym.dto.LoginResponseDto;
import com.app.gym.dto.LoginResponseDto;
import com.app.gym.infra.security.TokenService;
import com.app.gym.model.User;
import com.app.gym.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid CreateUserRequestDto createUserRequestDto) {
        User newUser = userService.createUser(createUserRequestDto);

        return ResponseEntity.ok().build();
    }
}