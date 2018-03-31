package com.javaedge.concurrency.example.mq.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author JavaEdge
 * @date 2019-02-20
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue queue() {
        return new Queue(QueueConstants.TEST);
    }
}



