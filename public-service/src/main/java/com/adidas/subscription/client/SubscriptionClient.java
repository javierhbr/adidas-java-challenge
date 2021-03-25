package com.adidas.subscription.client;


import com.adidas.subscription.dto.SubscriptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "subscriptionClient", url = "${subscription-service.url}")
public interface SubscriptionClient {

    @PostMapping(value = "${subscription-service.register-subscription.uri}", consumes = "application/json")
    SubscriptionDto registerSubscription(@RequestBody SubscriptionDto request);

    @DeleteMapping(value = "${subscription-service.remove-subscription.uri}", consumes = "application/json")
    void removeSubscription(@PathVariable("subscriptionId") String subscriptionId);

    @GetMapping(value = "${subscription-service.subscription-detail.uri}", consumes = "application/json")
    SubscriptionDto subscriptionDetail(@PathVariable("subscriptionId") String subscriptionId);

    @GetMapping(value = "${subscription-service.all-subscription.uri}", consumes = "application/json")
    List<SubscriptionDto> allSubscription(@RequestParam int page, @RequestParam int pageSize);
}
