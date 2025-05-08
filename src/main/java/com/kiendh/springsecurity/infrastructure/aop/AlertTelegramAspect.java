package com.kiendh.springsecurity.infrastructure.aop;

import com.kiendh.springsecurity.config.security.CustomUserDetail;
import com.kiendh.springsecurity.dto.enums.AlertType;
import com.kiendh.springsecurity.dto.response.AlertDto;
import com.kiendh.springsecurity.exception.CustomException;
import com.kiendh.springsecurity.service.TelegramAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AlertTelegramAspect {

    private final TelegramAlertService telegramAlertService;

    @Around("@annotation(AlertTelegram)")
    public Object handleAlertTelegram(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AlertTelegram alertTelegram = method.getDeclaredAnnotation(AlertTelegram.class);

        if (alertTelegram == null) {
            return joinPoint.proceed();
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            try {
                CustomUserDetail customUserDetail = SecurityContextHolder.getContext().getAuthentication() != null
                        ? (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                        : null;

                String username = customUserDetail != null ? customUserDetail.getUsername() : "unknown";
                String fullname = customUserDetail != null ? customUserDetail.getEmail() : "unknown";

                if (e instanceof CustomException) {
                    telegramAlertService.sendMessage(AlertDto.builder()
                            .title(alertTelegram.content())
                            .alertType(AlertType.ERROR)
                            .content(e.getMessage())
                            .fullName(fullname)
                            .traceId(UUID.randomUUID().toString())
                            .timestamp(LocalDateTime.now())
                            .username(username)
                            .build());
                } else {
                    telegramAlertService.sendMessage(AlertDto.builder()
                            .title(alertTelegram.content())
                            .alertType(AlertType.ERROR)
                            .content("Server Error")
                            .fullName(fullname)
                            .traceId(UUID.randomUUID().toString())
                            .timestamp(LocalDateTime.now())
                            .username(username)
                            .build());
                }
            } catch (Exception ex) {
                log.error("Error while sending alert: {}", ex.getMessage(), ex);
            }
            throw e;
        }
    }
}
