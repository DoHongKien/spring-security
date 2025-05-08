package com.kiendh.springsecurity.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.mission}")
    public String exchangeMission;

    @Value("${rabbitmq.exchange.game}")
    public String exchangeGame;

    @Value("${rabbitmq.exchange.reward}")
    public String exchangeReward;

    @Value("${rabbitmq.queue.mission.check-in}")
    public String queueMissionCheckIn;

    @Value("${rabbitmq.queue.mission.online-time}")
    public String queueMissionOnlineTime;

    @Value("${rabbitmq.queue.mission.top-score}")
    public String queueMissionTopScore;

    @Value("${rabbitmq.queue.mission.top-game}")
    public String queueMissionTopGame;

    @Value("${rabbitmq.queue.mission.play-first}")
    private String queueMissionPlayFirst;

    @Bean
    public Exchange topicExchange() {
        return new TopicExchange(exchangeMission, true, false);
    }

    @Bean
    public Queue queueMissionCheckIn() {
        return new Queue(queueMissionCheckIn, true);
    }

    @Bean
    public Queue queueMissionOnlineTime() {
        return new Queue(queueMissionOnlineTime, true);
    }

    @Bean
    public Queue queueMissionTopScore() {
        return new Queue(queueMissionTopScore, true);
    }

    @Bean
    public Queue queueMissionTopGame() {
        return new Queue(queueMissionTopGame, true);
    }

    @Bean
    public Queue queueMissionPlayFirst() {
        return new Queue(queueMissionPlayFirst, true);
    }

    @Bean
    public Binding bindingMissionCheckIn(Queue queueMissionCheckIn, Exchange exchangeMission) {
        return BindingBuilder.bind(queueMissionCheckIn).to(exchangeMission)
                .with("mission.*.gate").noargs();
    }

    @Bean
    public Binding bindingMissionOnlineTime(Queue queueMissionOnlineTime, Exchange exchangeMission) {
        return BindingBuilder.bind(queueMissionOnlineTime).to(exchangeMission)
                .with("mission.*.gate").noargs();
    }

    @Bean
    public Binding bindingMissionTopScore(Queue queueMissionTopScore, Exchange exchangeMission) {
        return BindingBuilder.bind(queueMissionTopScore).to(exchangeMission)
                .with("mission.*.game").noargs();
    }

    @Bean
    public Binding bindingMissionTopGame(Queue queueMissionTopGame, Exchange exchangeMission) {
        return BindingBuilder.bind(queueMissionTopGame).to(exchangeMission)
                .with("mission.*.game").noargs();
    }

    @Bean
    public Binding bindingMissionPlayFirst(Queue queueMissionPlayFirst, Exchange exchangeMission) {
        return BindingBuilder.bind(queueMissionPlayFirst).to(exchangeMission)
                .with("mission.*.game").noargs();
    }
}
