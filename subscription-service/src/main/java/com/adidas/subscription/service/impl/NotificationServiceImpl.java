package com.adidas.subscription.service.impl;

import com.adidas.subscription.dto.NotificationMessageDto;
import com.adidas.subscription.service.NotificationService;
import com.adidas.subscription.service.RabbitMQSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final RabbitMQSenderService senderService;

    @Autowired
    public NotificationServiceImpl(RabbitMQSenderService senderService) {
        this.senderService = senderService;
    }

    @Override
    public void sendNotification(NotificationMessageDto message) {
        try {
            this.senderService.pushMessage(message);
        }catch (Exception ex){
            logger.error("Error sending email notification:",ex);
        }
    }
}
