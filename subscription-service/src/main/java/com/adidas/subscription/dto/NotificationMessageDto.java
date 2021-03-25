package com.adidas.subscription.dto;

import com.adidas.subscription.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationMessageDto {

    private NotificationType notificationType;
    private String destination;
    private String message;

}
