package com.kiendh.springsecurity.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TokenInfoRequest {

    private String accessToken;
    private String refreshToken;
    private String deviceId;
}
