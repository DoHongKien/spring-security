package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.dto.request.RefreshTokenRequest;
import com.kiendh.springsecurity.entity.RefreshToken;

public interface RefreshTokenService {

    RefreshToken findByToken(String token);

    RefreshToken save(RefreshTokenRequest refreshToken);
}
