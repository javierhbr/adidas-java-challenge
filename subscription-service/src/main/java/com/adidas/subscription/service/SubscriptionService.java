package com.adidas.subscription.service;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;

import java.util.List;

public interface SubscriptionService {
    SubscriptionDto registerSubscription(SubscriptionDto subscription) throws SubscriptionGenericException;

    void removeSubscription(String subscriptionId) throws SubscriptionGenericException, NoSubscriptionException;

    SubscriptionDto retrieveSubscriptionDetail(String subscriptionId) throws SubscriptionGenericException, NoSubscriptionException;

    List<SubscriptionDto> allSubscription(int page, int pageSize) throws SubscriptionGenericException, NoSubscriptionException;
}
