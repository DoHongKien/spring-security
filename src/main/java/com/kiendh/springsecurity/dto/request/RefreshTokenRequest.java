package com.kiendh.springsecurity.dto.request;

import lombok.*;
import jakarta.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenRequest {

    @NotNull(message = "Refresh token is required")
    private String refreshToken;

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Device id is required")
    private String deviceId;
}
