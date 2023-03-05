package main.java.com.javaedge.concurrency.mq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

/**
 * @author JavaEdge
 * @date 2019-02-20
 */
@Slf4j
@Configuration
public class RabbitMqServer {

    @RabbitListener(queues = QueueConstants.TEST)
    public void receive(String message) {
        log.info("{}", message);
    }
}
