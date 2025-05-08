package com.kiendh.springsecurity.controller;

import com.kiendh.springsecurity.dto.request.LoginRequest;
import com.kiendh.springsecurity.dto.request.RequiredRefreshRequest;
import com.kiendh.springsecurity.dto.request.UserRequest;
import com.kiendh.springsecurity.dto.response.LoginResponse;
import com.kiendh.springsecurity.dto.response.UserResponse;
import com.kiendh.springsecurity.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("register")
    public UserResponse register(@RequestBody @Valid UserRequest userRequest) {
        return authenticationService.register(userRequest);
    }

    @PostMapping("refresh")
    public LoginResponse refreshToken(@RequestBody RequiredRefreshRequest requiredRefreshRequest) {
        return authenticationService.refreshToken(requiredRefreshRequest);
    }
}
