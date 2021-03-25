package com.adidas.subscription.dto;

import com.adidas.subscription.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NotificationDto {
    private NotificationType notificationType;
    private String destination;
    private String message;
}
