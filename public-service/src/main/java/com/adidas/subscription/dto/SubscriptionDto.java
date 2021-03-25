package com.adidas.subscription.dto;

import com.adidas.subscription.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubscriptionDto {

    private String id;
    @NotNull
    @Email()
    private String email;
    private String firstName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBird;
    @NotNull
    private boolean isConsent;

    private GenderEnum gender;
    @NotNull
    private String newsletterId;

}
