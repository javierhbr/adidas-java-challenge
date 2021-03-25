package com.adidas.subscription.exception;

import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.enums.ErrorCodeEnum;

public class NoSubscriptionException extends Exception{

    private final ErrorInfo error;

    public NoSubscriptionException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.error = ErrorInfo.builder().code(errorCode.code()).message(message).build();
    }

    public ErrorInfo getError() {
        return error;
    }
}
