package com.adidas.subscription.service;

import com.adidas.subscription.dto.NotificationMessageDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQSenderService {

    private final AmqpTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    @Autowired
    public RabbitMQSenderService(AmqpTemplate rabbitTemplate,
                                 @Value("${notification-service.exchange}") String exchange,
                                 @Value("${notification-service.routingKey}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void pushMessage(NotificationMessageDto message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
