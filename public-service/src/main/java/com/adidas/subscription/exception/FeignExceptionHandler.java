package com.adidas.subscription.exception;

import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.enums.ErrorCodeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class FeignExceptionHandler {
    private final List<Integer> allowedHttpStatuses = Arrays.asList(200, 201, 204, 500, 400, 404);
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(FeignExceptionHandler.class);


    public FeignExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void validateFeignException(FeignException fe, ErrorCodeEnum errorCode) throws DownstreamServicesException {
        if (!this.allowedHttpStatuses.contains(fe.status())) {
            throw new DownstreamServicesException(fe.getMessage(), errorCode);
        }
    }

    public Optional<ErrorInfo> processFeignException(FeignException fe) {
        ErrorInfo error = null;

            try {
                error = this.objectMapper.readValue(fe.contentUTF8(), ErrorInfo.class);

            } catch (JsonProcessingException ex) {
                log.error("Error when parsing FeignException body response:", ex);
            }
            return Optional.ofNullable(error);
    }
}
