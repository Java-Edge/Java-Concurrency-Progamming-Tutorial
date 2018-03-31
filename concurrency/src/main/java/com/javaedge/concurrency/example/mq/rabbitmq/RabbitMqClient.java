package com.javaedge.concurrency.example.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author JavaEdge
 * @date 2019-02-20
 */
@Slf4j
@Configuration
public class RabbitMqClient {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(QueueConstants.TEST,message);
    }
}



