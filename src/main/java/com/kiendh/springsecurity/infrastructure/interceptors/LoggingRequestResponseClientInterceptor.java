package com.kiendh.springsecurity.infrastructure.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoggingRequestResponseClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        long startTime = System.currentTimeMillis();

        if (log.isInfoEnabled()) {
            log.info("HTTP Request ==> {} {} {}", request.getMethod(), request.getURI(),
                    new String(body, StandardCharsets.UTF_8));
        }

        try {
            return execution.execute(request, body);
        } finally {
            if (log.isInfoEnabled()) {
                log.info("HTTP Request ==> {} take {}ms times",
                        request.getURI(), (System.currentTimeMillis() - startTime));
            }
        }
    }
}
