package com.adidas.subscription.model;

import com.adidas.subscription.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionModel {

    @Id
    private String id;
    private String email;
    private String firstName;
    private LocalDate dateOfBird;
    private boolean isConsent;

    private GenderEnum gender;

    private String newsletterId;
}
