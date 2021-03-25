package com.adidas.subscription.service;

import com.adidas.subscription.client.SubscriptionClient;
import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.enums.ErrorCodeEnum;
import com.adidas.subscription.exception.DownstreamServicesException;
import com.adidas.subscription.exception.FeignExceptionHandler;
import com.adidas.subscription.exception.SubscriptionGenericException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionClient subscriptionClient;
    private final FeignExceptionHandler feignExceptionHandler;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionClient subscriptionClient, FeignExceptionHandler feignExceptionHandler) {
        this.subscriptionClient = subscriptionClient;
        this.feignExceptionHandler = feignExceptionHandler;
    }

    @Override
    public SubscriptionDto registerSubscription(SubscriptionDto subscription) throws Exception {
        try {
            return this.subscriptionClient.registerSubscription(subscription);
        } catch (FeignException fe) {
            feignExceptionHandler.validateFeignException(fe, ErrorCodeEnum.REGISTRATION_DOWNSTREAM_ERROR);
            throw fe;
        }
    }

    @Override
    public void removeSubscription(String subscriptionId) throws Exception {
        try {
            this.subscriptionClient.removeSubscription(subscriptionId);
        } catch (FeignException fe) {
            feignExceptionHandler.validateFeignException(fe, ErrorCodeEnum.REGISTRATION_DOWNSTREAM_ERROR);
            throw fe;
        }
    }

    @Override
    public SubscriptionDto retrieveSubscriptionDetail(String subscriptionId) throws Exception {
        try {
            return this.subscriptionClient.subscriptionDetail(subscriptionId);
        } catch (FeignException fe) {
            feignExceptionHandler.validateFeignException(fe, ErrorCodeEnum.REGISTRATION_DOWNSTREAM_ERROR);
            throw fe;
        }
    }

    @Override
    public List<SubscriptionDto> allSubscription(int page, int pageSize) throws Exception {
        try {
            return this.subscriptionClient.allSubscription(page, pageSize);
        } catch (FeignException fe) {
            feignExceptionHandler.validateFeignException(fe, ErrorCodeEnum.REGISTRATION_DOWNSTREAM_ERROR);
            throw fe;
        }
    }
}
