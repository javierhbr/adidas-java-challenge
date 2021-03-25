package com.adidas.subscription.service;

import com.adidas.subscription.dto.NotificationMessageDto;

public interface NotificationService {
    void sendNotification(NotificationMessageDto message);
}
