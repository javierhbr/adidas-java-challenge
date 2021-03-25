package com.adidas.subscription.service.mapper;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.enums.GenderEnum;
import com.adidas.subscription.model.SubscriptionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionMapperTest {
    private SubscriptionMapper subscriptionMapper;
    @BeforeEach
    void setUp() {
        this.subscriptionMapper = new SubscriptionMapper();
    }

    @Test
    void mapModelToDto() {
        SubscriptionModel model = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        SubscriptionDto result = this.subscriptionMapper.mapModelToDto(model);

        assertEquals(result.getId(), model.getId());
        assertEquals(result.getEmail(), model.getEmail());
        assertEquals(result.isConsent(), model.isConsent());
        assertEquals(result.getGender(), model.getGender());
    }

    @Test
    void mapDtoToModel() {

        SubscriptionDto dto = SubscriptionDto.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        SubscriptionModel result = this.subscriptionMapper.mapDtoToModel(dto);

        assertEquals(result.getId(), dto.getId());
        assertEquals(result.getEmail(), dto.getEmail());
        assertEquals(result.isConsent(), dto.isConsent());
        assertEquals(result.getGender(), dto.getGender());
    }
}