package com.kiendh.springsecurity.repository;

import com.kiendh.springsecurity.dto.enums.RevokedStatus;
import com.kiendh.springsecurity.entity.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenAndIsRevoked(String token, RevokedStatus revoked);

    Optional<RefreshToken> findByUserIdAndDeviceId(@NotNull(message = "User id is required") Long userId, @NotNull(message = "Device id is required") String deviceId);
}