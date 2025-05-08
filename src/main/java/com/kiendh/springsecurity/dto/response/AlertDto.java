package com.kiendh.springsecurity.dto.response;

import com.kiendh.springsecurity.dto.enums.AlertType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AlertDto {

    private LocalDateTime timestamp;
    private String traceId;
    private AlertType alertType;
    private String title;
    private String content;
    private String username;
    private String fullName;
}
