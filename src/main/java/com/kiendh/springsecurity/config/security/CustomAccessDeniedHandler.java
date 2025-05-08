package com.kiendh.springsecurity.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiendh.springsecurity.dto.response.ResponseDto;
import com.kiendh.springsecurity.dto.enums.ResponseStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setTimestamp(LocalDateTime.now());
        responseDto.setTraceId(MDC.get("traceId"));
        responseDto.setCode(ResponseStatus.ACCESS_DENIED.getCode());
        responseDto.setMessage(ResponseStatus.ACCESS_DENIED.getMessage());
        response.getOutputStream().println(objectMapper.writeValueAsString(responseDto));
    }
}
