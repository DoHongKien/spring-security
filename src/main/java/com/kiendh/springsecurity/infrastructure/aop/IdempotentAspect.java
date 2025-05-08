package com.kiendh.springsecurity.infrastructure.aop;

import com.kiendh.springsecurity.exception.CustomException;
import com.kiendh.springsecurity.exception.DuplicateRequestException;
import com.kiendh.springsecurity.service.IdempotentService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final IdempotentService idempotentService;

    @Around("@annotation(IdempotentRequest)")
    public Object handleIdempotentRequest(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        IdempotentRequest idempotentRequest = method.getDeclaredAnnotation(IdempotentRequest.class);

        if (idempotentRequest != null) {
            Object[] args = joinPoint.getArgs();

            StandardEvaluationContext context = new StandardEvaluationContext();
            String[] parameterNames = methodSignature.getParameterNames();

            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            ExpressionParser parser = new SpelExpressionParser();
            Object value = parser.parseExpression(idempotentRequest.key())
                    .getValue(context);

            // Evaluate the expression
            boolean success = idempotentService
                    .markExecuted(Objects.requireNonNull(value).toString());

            if (Boolean.FALSE.equals(success)) {
                throw new DuplicateRequestException("Duplicate request");
            }
        }

        return joinPoint.proceed();
    }
}
