package com.kiendh.springsecurity.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiendh.springsecurity.config.jwt.JwtService;
import com.kiendh.springsecurity.config.security.CustomUserDetail;
import com.kiendh.springsecurity.dto.enums.AlertType;
import com.kiendh.springsecurity.dto.request.*;
import com.kiendh.springsecurity.dto.response.AlertDto;
import com.kiendh.springsecurity.dto.response.LoginResponse;
import com.kiendh.springsecurity.dto.response.UserResponse;
import com.kiendh.springsecurity.entity.User;
import com.kiendh.springsecurity.exception.CustomException;
import com.kiendh.springsecurity.infrastructure.aop.AlertTelegram;
import com.kiendh.springsecurity.repository.UserRepository;
import com.kiendh.springsecurity.service.AuthenticationService;
import com.kiendh.springsecurity.service.CheckBitMapService;
import com.kiendh.springsecurity.service.RefreshTokenService;
import com.kiendh.springsecurity.service.TelegramAlertService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;
    private final HttpServletRequest request;
    private final CheckBitMapService checkService;
    private final TelegramAlertService telegramAlertService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        UserResponse userResponse = new UserResponse(user);

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            TokenInfoRequest tokenInfoRequest = genTokenInfo(customUserDetail, userResponse);

            RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                    .refreshToken(tokenInfoRequest.getAccessToken())
                    .deviceId(tokenInfoRequest.getDeviceId())
                    .userId(user.getId())
                    .build();

            refreshTokenService.save(refreshTokenRequest);

            return new LoginResponse(tokenInfoRequest.getAccessToken(), tokenInfoRequest.getRefreshToken());
        }
        return null;
    }

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (checkService.isUsernameExists(userRequest.getUsername())) {
            throw new CustomException("Username already exists");
        }

        if (checkService.isEmailExists(userRequest.getEmail())) {
            throw new CustomException("Email already exists");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setStatus("ACTIVE");

        userRepository.save(user);
        checkService.addUsername(userRequest.getUsername());
        checkService.addEmail(userRequest.getEmail());
        return new UserResponse(user);
    }

    @Override
    public LoginResponse refreshToken(RequiredRefreshRequest requiredRefreshRequest) {
        var refreshToken = refreshTokenService.findByToken(requiredRefreshRequest.getToken());

        var user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CustomUserDetail customUserDetail = new CustomUserDetail(user);
        UserResponse userResponse = new UserResponse(user);

        Date expiration = jwtService.extractExpiration(refreshToken.getToken());

        log.info("Expiration: {} ", expiration);

        if (expiration.after(new Date())) {

            TokenInfoRequest tokenInfoRequest = genTokenInfo(customUserDetail, userResponse);

            RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                    .refreshToken(tokenInfoRequest.getRefreshToken())
                    .deviceId(tokenInfoRequest.getDeviceId())
                    .userId(user.getId())
                    .build();
            refreshTokenService.save(refreshTokenRequest);

            return new LoginResponse(tokenInfoRequest.getAccessToken(), tokenInfoRequest.getRefreshToken());
        }
        throw new RuntimeException("Token is not expired yet");
    }

    private TokenInfoRequest genTokenInfo(CustomUserDetail customUserDetail, UserResponse userResponse) {
        return TokenInfoRequest.builder()
                .accessToken(jwtService.genToken(objectMapper.convertValue(userResponse, new TypeReference<>() {
                }), customUserDetail))
                .refreshToken(jwtService.genRefreshToken(customUserDetail))
                .deviceId(request.getHeader("Device-Id"))
                .build();
    }
}
