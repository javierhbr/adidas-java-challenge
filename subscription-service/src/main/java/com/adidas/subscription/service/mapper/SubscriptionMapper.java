package com.adidas.subscription.service.mapper;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.model.SubscriptionModel;

public class SubscriptionMapper {

    public SubscriptionDto mapModelToDto(SubscriptionModel dto) {
        return SubscriptionDto.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .dateOfBird(dto.getDateOfBird())
                .isConsent(dto.isConsent())
                .gender(dto.getGender())
                .newsletterId(dto.getNewsletterId())
                .build();
    }

    public SubscriptionModel mapDtoToModel(SubscriptionDto model) {
        return SubscriptionModel.builder()
                .id(model.getId())
                .email(model.getEmail())
                .firstName(model.getFirstName())
                .dateOfBird(model.getDateOfBird())
                .isConsent(model.isConsent())
                .gender(model.getGender())
                .newsletterId(model.getNewsletterId())
                .build();
    }
}
