package com.adidas.subscription.service.impl;

import com.adidas.subscription.dto.NotificationMessageDto;
import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.enums.ErrorCodeEnum;
import com.adidas.subscription.enums.NotificationType;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import com.adidas.subscription.model.SubscriptionModel;
import com.adidas.subscription.repository.SubscriptionRepository;
import com.adidas.subscription.service.NotificationService;
import com.adidas.subscription.service.SubscriptionService;
import com.adidas.subscription.service.mapper.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;
    private final SubscriptionMapper subscriptionMapper;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
                                   NotificationService notificationService) {
        this.subscriptionRepository = subscriptionRepository;
        this.notificationService = notificationService;
        this.subscriptionMapper = new SubscriptionMapper();
    }

    @Override
    public SubscriptionDto registerSubscription(SubscriptionDto subscription) throws SubscriptionGenericException {
        try {
            SubscriptionModel savedSubscription = this.subscriptionRepository.save(this.subscriptionMapper.mapDtoToModel(subscription));
            this.notificationService.sendNotification(NotificationMessageDto.builder()
                    .destination(savedSubscription.getEmail())
                    .message("the newsletter was successfully registered")
                    .notificationType(NotificationType.EMAIL)
                    .build());

            return this.subscriptionMapper.mapModelToDto(savedSubscription);
        } catch (Exception ex) {

            throw new SubscriptionGenericException(ErrorCodeEnum.REGISTRATION_ERROR, "oOps. we couldn't register to a newsletter.");
        }
    }

    @Override
    public void removeSubscription(String subscriptionId) throws SubscriptionGenericException, NoSubscriptionException {

        SubscriptionModel subscriptionToRemove =this.subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() ->new NoSubscriptionException(ErrorCodeEnum.NO_SUBSCRIPTION_FOUND_ERROR, "Oops. we couldn't find a subscription to remove."));
        try {
            this.subscriptionRepository.deleteById(subscriptionId);

            this.notificationService.sendNotification(NotificationMessageDto.builder()
                    .destination(subscriptionToRemove.getEmail())
                    .message("the newsletter was successfully registered")
                    .notificationType(NotificationType.EMAIL)
                    .build());
        } catch (Exception ex) {
            throw new SubscriptionGenericException(ErrorCodeEnum.CANCELLATION_ERROR, "Oops. we couldn't cancel the subscription.");
        }
    }

    @Override
    public SubscriptionDto retrieveSubscriptionDetail(String subscriptionId) throws SubscriptionGenericException, NoSubscriptionException {
        Optional<SubscriptionModel> result;
        try {
            result = this.subscriptionRepository.findById(subscriptionId);

        } catch (Exception ex) {
            throw new SubscriptionGenericException(ErrorCodeEnum.SUBSCRIPTION_DETAIL_ERROR, "Oops. we couldn't get the information.");
        }
        return result.map(subscriptionMapper::mapModelToDto)
                .orElseThrow(() -> new NoSubscriptionException(ErrorCodeEnum.NO_SUBSCRIPTION_FOUND_ERROR, "Oops. we couldn't find a subscription."));
    }

    @Override
    public List<SubscriptionDto> allSubscription(int page, int pageSize) throws SubscriptionGenericException, NoSubscriptionException {
        Page<SubscriptionModel> result;
        try {
            Pageable paging = PageRequest.of(page, pageSize);
            result = this.subscriptionRepository.findAll(paging);
        } catch (Exception ex) {
            throw new SubscriptionGenericException(ErrorCodeEnum.SUBSCRIPTION_DETAIL_ERROR, "Oops. we couldn't get all the subscription.");
        }

        if(result.getContent().isEmpty()){
            throw new NoSubscriptionException(ErrorCodeEnum.NO_SUBSCRIPTION_FOUND_ERROR, "Oops. we couldn't find any subscription.");
        }
        return result.getContent()
                .stream()
                .map(subscriptionMapper::mapModelToDto)
                .collect(Collectors.toList());
    }
}
