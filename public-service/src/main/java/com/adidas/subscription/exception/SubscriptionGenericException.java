package com.adidas.subscription.exception;

import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.enums.ErrorCodeEnum;

public class SubscriptionGenericException extends Exception{

    private final ErrorInfo error;

    public SubscriptionGenericException(ErrorCodeEnum errorCode, String message) {
        super(message);
        this.error = ErrorInfo.builder().code(errorCode.code()).message(message).build();
    }

    public SubscriptionGenericException(String message, ErrorInfo error) {
        super(message);
        this.error = error;
    }

    public SubscriptionGenericException(String message, Throwable cause, ErrorInfo error) {
        super(message, cause);
        this.error = error;
    }

    public ErrorInfo getError() {
        return error;
    }
}
