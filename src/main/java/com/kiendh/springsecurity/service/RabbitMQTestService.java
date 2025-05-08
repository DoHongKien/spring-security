package com.kiendh.springsecurity.service;

public interface RabbitMQTestService {

    void sendMessage(String message, String routingKey);
}
