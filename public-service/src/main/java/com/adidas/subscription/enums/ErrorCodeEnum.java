package com.adidas.subscription.enums;

public enum ErrorCodeEnum {
    REGISTRATION_ERROR ("1000"),
    REGISTRATION_DOWNSTREAM_ERROR("1100"),

    CANCELLATION_ERROR ("2000"),
    SUBSCRIPTION_DETAIL_ERROR ("3000"),
    NO_SUBSCRIPTION_FOUND_ERROR ("3100");

    private String code;

    ErrorCodeEnum(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
