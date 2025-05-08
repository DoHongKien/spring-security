package com.kiendh.springsecurity.controller;

import com.kiendh.springsecurity.service.RabbitMQTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RabbitMQTestController {

    private final RabbitMQTestService rabbitMQTestService;

    @GetMapping("/send-message")
    public String sendMessage(@RequestParam("m") String message, @RequestParam("r") String routingKey) {
        rabbitMQTestService.sendMessage(message, routingKey);

        return message;
    }
}
