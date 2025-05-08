package com.kiendh.springsecurity.infrastructure.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TrackTimeAspect {

    @Around("@annotation(TrackTimeRequest)")
    public Object trackExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        if (log.isInfoEnabled()) {
            log.info("Start request of {}.{} ===> {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), joinPoint.getArgs());
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("Error while processing request {}", joinPoint.getArgs(), e);
            throw e;
        } finally {
            if(log.isInfoEnabled()) {
                long endTime = System.currentTimeMillis();
                log.info("Execution time of {}.{}: {} ms", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), (endTime - startTime));
            }
        }
    }
}
