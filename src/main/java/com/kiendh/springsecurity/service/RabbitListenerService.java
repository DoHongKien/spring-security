package com.kiendh.springsecurity.service;

import com.kiendh.springsecurity.infrastructure.aop.TrackTimeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitListenerService {

    @TrackTimeRequest
    @RabbitListener(queues = "${rabbitmq.queue.mission.check-in}", concurrency = "3-5")
    public void orangeQueueListener(String message) {
        System.out.println("Received message from mission-check-in queue: " + message);
    }

    @TrackTimeRequest
    @RabbitListener(queues = "${rabbitmq.queue.mission.online-time}")
    public void orange1QueueListener(String message) {
        System.out.println("Received message from mission-online-time queue: " + message);
    }

    @TrackTimeRequest
    @RabbitListener(queues = "${rabbitmq.queue.mission.play-first}")
    public void greenQueueListener(String message) {
        System.out.println("Received message from mission-play-first queue: " + message);
    }

    @TrackTimeRequest
    @RabbitListener(queues = "${rabbitmq.queue.mission.top-game}")
    public void directQueueListener(String message) {
        System.out.println("Received message from mission-top-game queue: " + message);
    }

    @TrackTimeRequest
    @RabbitListener(queues = "${rabbitmq.queue.mission.top-score}")
    public void headersQueueListener(String message) {
        System.out.println("Received message from mision-top-score queue: " + message);
    }
}
