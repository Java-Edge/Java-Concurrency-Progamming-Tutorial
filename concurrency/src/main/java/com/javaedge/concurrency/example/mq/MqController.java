package com.javaedge.concurrency.example.mq;

import com.javaedge.concurrency.example.mq.kafka.KafkaSender;
import com.javaedge.concurrency.example.mq.rabbitmq.RabbitMqClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author JavaEdge
 * @date 2019-02-20
 */
@Controller
@RequestMapping("/mq")
public class MqController {

    @Resource
    private RabbitMqClient rabbitMqClient;

    @Resource
    private KafkaSender kafkaSender;

    @RequestMapping("/send")
    @ResponseBody
    public String send(@RequestParam("message") String message) {
        rabbitMqClient.send(message);
        kafkaSender.send(message);
        return "success";
    }
}
