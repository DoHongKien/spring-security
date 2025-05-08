package com.kiendh.springsecurity.service.impl;

import com.kiendh.springsecurity.exception.CustomException;
import com.kiendh.springsecurity.infrastructure.aop.AlertTelegram;
import com.kiendh.springsecurity.service.RabbitMQTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQTestServiceImpl implements RabbitMQTestService {

    @Value("${rabbitmq.exchange.mission}")
    private String exchangeMission;

    private final RabbitTemplate rabbitTemplate;

    @AlertTelegram(content = "Gửi dữ liệu đến RabbitMQ thất bại")
    @Override
    public void sendMessage(String message, String routingKey) {
        routingKey = null;
        if(routingKey.equalsIgnoreCase("game")) {
            throw new RuntimeException();
        }
        if (!"game".equalsIgnoreCase(routingKey) && !"gate".equalsIgnoreCase(routingKey)) {
            throw new CustomException("Invalid routing key");
        }
        rabbitTemplate.convertAndSend(exchangeMission,
                "mission.publish." + routingKey.toLowerCase(), message);
    }
}
