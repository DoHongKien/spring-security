package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.dto.request.LoginRequest;
import com.kiendh.springsecurity.dto.request.RequiredRefreshRequest;
import com.kiendh.springsecurity.dto.request.UserRequest;
import com.kiendh.springsecurity.dto.response.LoginResponse;
import com.kiendh.springsecurity.dto.response.UserResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refreshToken(RequiredRefreshRequest requiredRefreshRequest);

    UserResponse register(UserRequest userRequest);

//    void logout(String token);
//
//    boolean isValidToken(String token);
//
//    String getUsernameFromToken(String token);
//
//    String generateToken(String username);
//
//    String generateRefreshToken(String username);
//
//    boolean isTokenExpired(String token);
//
//    boolean isRefreshTokenExpired(String token);
//
//    boolean validateToken(String token, String username);
//
//    boolean validateRefreshToken(String token, String username);
//
//    boolean isTokenBlackListed(String token);
//
//    void addToBlackList(String token);
//
//    void removeFromBlackList(String token);
}
