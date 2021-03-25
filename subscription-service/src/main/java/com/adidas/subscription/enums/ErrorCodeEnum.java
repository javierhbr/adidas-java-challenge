package com.adidas.subscription.enums;

public enum ErrorCodeEnum {
    REGISTRATION_ERROR ("10000"),
    CANCELLATION_ERROR ("20000"),
    SUBSCRIPTION_DETAIL_ERROR ("30000"),
    NO_SUBSCRIPTION_FOUND_ERROR ("31000");

    private String code;

    ErrorCodeEnum(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
