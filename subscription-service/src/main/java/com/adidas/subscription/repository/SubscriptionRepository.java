package com.adidas.subscription.repository;

import com.adidas.subscription.model.SubscriptionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionRepository extends MongoRepository<SubscriptionModel, String> {

    @Override
    Page<SubscriptionModel> findAll( Pageable pageable);
}
