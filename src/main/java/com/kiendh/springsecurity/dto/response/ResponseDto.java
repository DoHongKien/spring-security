package com.kiendh.springsecurity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {

  private LocalDateTime timestamp;
  private String traceId;
  private String code;
  private String message;
  private T data;
}
