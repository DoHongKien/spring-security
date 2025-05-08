package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.dto.enums.RevokedStatus;
import com.kiendh.springsecurity.dto.request.RefreshTokenRequest;
import com.kiendh.springsecurity.entity.RefreshToken;
import com.kiendh.springsecurity.repository.RefreshTokenRepository;
import com.kiendh.springsecurity.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByTokenAndIsRevoked(token, RevokedStatus.NOT_REVOKED)
                .orElseThrow(() -> new RuntimeException("Token not found"));
    }

    @Override
    public RefreshToken save(RefreshTokenRequest request) {

        var refreshToken = refreshTokenRepository.findByUserIdAndDeviceId(request.getUserId(), request.getDeviceId())
                .orElse(new RefreshToken());
        refreshToken.setToken(request.getRefreshToken());
        refreshToken.setDeviceId(request.getDeviceId());
        refreshToken.setUserId(request.getUserId());
        refreshToken.setIsRevoked(RevokedStatus.NOT_REVOKED);

        return refreshTokenRepository.save(refreshToken);
    }
}
