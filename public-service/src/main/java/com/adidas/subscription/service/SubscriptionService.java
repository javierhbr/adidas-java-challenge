package com.adidas.subscription.service;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.exception.DownstreamServicesException;
import com.adidas.subscription.exception.SubscriptionGenericException;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto registerSubscription(SubscriptionDto subscription) throws Exception;

    void removeSubscription(String subscriptionId) throws Exception;

    SubscriptionDto retrieveSubscriptionDetail(String subscriptionId) throws Exception;

    List<SubscriptionDto> allSubscription(int page, int pageSize) throws Exception;
}
