package com.kiendh.springsecurity.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kiendh.springsecurity.dto.response.ResponseDto;
import com.kiendh.springsecurity.dto.enums.ResponseStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseDto<Object> responseDto = new ResponseDto<>();
        responseDto.setTimestamp(LocalDateTime.now());
        responseDto.setTraceId(MDC.get("traceId"));
        responseDto.setCode(ResponseStatus.UNAUTHORIZED.getCode());
        responseDto.setMessage(ResponseStatus.UNAUTHORIZED.getMessage());
        response.getOutputStream().println(objectMapper.writeValueAsString(responseDto));
    }
}
