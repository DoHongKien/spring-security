package com.kiendh.springsecurity.dto.enums;

import lombok.Getter;

@Getter
public enum ResponseStatus {
  SUCCESS("success", "0000"),
  ACCESS_DENIED("access denied", "0001"),
  UNAUTHORIZED("unauthorized", "0002"),
  ERROR("error", "9999");

  private final String code;
  private final String message;

  ResponseStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
