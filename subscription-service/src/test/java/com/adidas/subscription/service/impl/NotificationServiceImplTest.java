package com.adidas.subscription.service.impl;

import com.adidas.subscription.dto.NotificationMessageDto;
import com.adidas.subscription.enums.NotificationType;
import com.adidas.subscription.service.RabbitMQSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {
    private NotificationServiceImpl notificationService;
    private RabbitMQSenderService rabbitMQSenderService;
    @BeforeEach
    void setUp() {
        this.rabbitMQSenderService = mock(RabbitMQSenderService.class);
        this.notificationService = new NotificationServiceImpl(this.rabbitMQSenderService);
    }

    @Test
    void shouldNotThrowErrorFromSendNotification() {
        NotificationMessageDto messageDto = NotificationMessageDto.builder()
                .destination("destination")
                .message("message")
                .notificationType(NotificationType.EMAIL)
                .build();

        doThrow(IllegalArgumentException.class)
                .when(rabbitMQSenderService).pushMessage(any(NotificationMessageDto.class));
        assertDoesNotThrow(() ->this.notificationService.sendNotification(messageDto));
        verify(rabbitMQSenderService).pushMessage(messageDto);
    }

    @Test
    void shouldCompleteSendNotification() {
        NotificationMessageDto messageDto = NotificationMessageDto.builder()
                .destination("destination")
                .message("message")
                .notificationType(NotificationType.EMAIL)
                .build();

        doNothing()
                .when(rabbitMQSenderService).pushMessage(any(NotificationMessageDto.class));
        assertDoesNotThrow(() ->this.notificationService.sendNotification(messageDto));
        verify(rabbitMQSenderService).pushMessage(messageDto);
    }
}