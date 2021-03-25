package com.adidas.subscription.controller;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import com.adidas.subscription.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscriptions")
    public
    @ResponseBody
    SubscriptionDto registerSubscription(@Valid @RequestBody SubscriptionDto subscription) throws Throwable {
        return this.subscriptionService.registerSubscription(subscription);
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public
    @ResponseBody
    void removeSubscription(@PathVariable("subscriptionId") String subscriptionId) throws Throwable {
        this.subscriptionService.removeSubscription(subscriptionId);
    }

    @GetMapping("/subscriptions/{subscriptionId}")
    public
    @ResponseBody
    SubscriptionDto detailSubscription(@PathVariable("subscriptionId") String subscriptionId) throws Throwable {
       return this.subscriptionService.retrieveSubscriptionDetail(subscriptionId);
    }

    @GetMapping("/subscriptions")
    public
    @ResponseBody
    List<SubscriptionDto> allSubscription(@RequestParam int page, @RequestParam int pageSize) throws Throwable {
        return this.subscriptionService.allSubscription(page, pageSize);
    }
}
