package com.adidas.subscription.service;

import com.adidas.subscription.dto.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener{

    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @RabbitListener(queues = "${notification-service.queue}")
    public void onMessage(NotificationDto message) {
        logger.info("processing Notification - {}" , message);
    }

}


