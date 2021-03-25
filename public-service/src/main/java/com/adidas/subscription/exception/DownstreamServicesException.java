package com.adidas.subscription.exception;

import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.enums.ErrorCodeEnum;


public class DownstreamServicesException extends Exception {
    private final ErrorInfo error;

    public DownstreamServicesException(String message, ErrorCodeEnum errorCode) {
        super(message);
        this.error = ErrorInfo.builder().code(errorCode.code()).message(message).build();
    }

    public DownstreamServicesException(ErrorInfo error) {
        super(error.getMessage());
        this.error = error;
    }

    public ErrorInfo getError() {
        return error;
    }
}
