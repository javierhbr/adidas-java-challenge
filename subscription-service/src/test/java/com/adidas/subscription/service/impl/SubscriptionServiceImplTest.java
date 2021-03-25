package com.adidas.subscription.service.impl;

import com.adidas.subscription.dto.NotificationMessageDto;
import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.enums.GenderEnum;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import com.adidas.subscription.model.SubscriptionModel;
import com.adidas.subscription.repository.SubscriptionRepository;
import com.adidas.subscription.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionServiceImplTest {
    private SubscriptionServiceImpl subscriptionService;
    private SubscriptionRepository subscriptionRepository;
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        this.subscriptionRepository = mock(SubscriptionRepository.class);
        this.notificationService = mock(NotificationService.class);
        this.subscriptionService = new SubscriptionServiceImpl(this.subscriptionRepository, this.notificationService);
        doNothing().when(notificationService).sendNotification(any(NotificationMessageDto.class));
    }

    @Test
    void shouldRegisterSubscriptionHandlerException() {
        when(subscriptionRepository.save(any(SubscriptionModel.class)))
                .thenThrow(IllegalArgumentException.class);

        SubscriptionDto newSubscriptionDto = SubscriptionDto.builder()
                .email("email@mail.com")
                .firstName("test")
                .build();
        Throwable thrown = assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionService.registerSubscription(newSubscriptionDto));

        assertThat(thrown, instanceOf(SubscriptionGenericException.class));
        verify(subscriptionRepository).save(any(SubscriptionModel.class));
    }

    @Test
    void shouldReturnSubscriptionAfterRegisterSubscription() throws SubscriptionGenericException {
        SubscriptionModel subscriptionModelMock = SubscriptionModel.builder()
                .id("mockId")
                .email("email@email.com")
                .firstName("test")
                .build();
        when(subscriptionRepository.save(any(SubscriptionModel.class)))
                .thenReturn(subscriptionModelMock);

        SubscriptionDto newSubscriptionDto = SubscriptionDto.builder()
                .email("email@email.com")
                .firstName("test")
                .build();
        SubscriptionDto savedSubscription = this.subscriptionService.registerSubscription(newSubscriptionDto);

        assertThat(savedSubscription, is(notNullValue()));
        assertEquals(savedSubscription.getId(), "mockId");
        assertEquals(savedSubscription.getEmail(), newSubscriptionDto.getEmail());
        assertEquals(savedSubscription.isConsent(), newSubscriptionDto.isConsent());
        assertEquals(savedSubscription.getGender(), newSubscriptionDto.getGender());
        verify(subscriptionRepository).save(any(SubscriptionModel.class));
        verify(notificationService).sendNotification(any(NotificationMessageDto.class));
    }


    @Test
    void shouldRemoveSubscriptionReturnNoSubscriptionException() {
        String subscriptionId = "mockSubscriptionId";

        when(subscriptionRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        doThrow(IllegalArgumentException.class)
                .when(subscriptionRepository).deleteById(anyString());
        Throwable thrown = assertThrows(NoSubscriptionException.class, () ->
                this.subscriptionService.removeSubscription(subscriptionId));

        assertThat(thrown, instanceOf(NoSubscriptionException.class));
        verify(subscriptionRepository).findById(subscriptionId);
        verifyNoMoreInteractions(subscriptionRepository);
    }

    @Test
    void shouldRemoveSubscriptionReturnSubscriptionGenericException() {
        String subscriptionId = "mockSubscriptionId";
        SubscriptionModel modelMock = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        when(subscriptionRepository.findById(anyString()))
                .thenReturn(Optional.of(modelMock));

        doThrow(IllegalArgumentException.class)
                .when(subscriptionRepository).deleteById(anyString());
        Throwable thrown = assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionService.removeSubscription(subscriptionId));

        assertThat(thrown, instanceOf(SubscriptionGenericException.class));
        verify(subscriptionRepository).deleteById(subscriptionId);
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void shouldRemoveSubscriptionReturnNoException() {
        String subscriptionId = "mockSubscriptionId";
        SubscriptionModel modelMock = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        when(subscriptionRepository.findById(anyString()))
                .thenReturn(Optional.of(modelMock));
        doNothing()
                .when(subscriptionRepository).deleteById(anyString());

        assertDoesNotThrow( () -> this.subscriptionService.removeSubscription(subscriptionId));
        verify(subscriptionRepository).deleteById(subscriptionId);
        verify(notificationService).sendNotification(any(NotificationMessageDto.class));
    }

    @Test
    void shouldReturnSubscriptionGenericExceptionFromRetrieveSubscriptionDetail() {
        String subscriptionId = "mockSubscriptionId";
        when(subscriptionRepository.findById(anyString()))
                .thenThrow(IllegalArgumentException.class);
        Throwable thrown = assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionService.retrieveSubscriptionDetail(subscriptionId));

        assertThat(thrown, instanceOf(SubscriptionGenericException.class));
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void shouldReturnNoSubscriptionExceptionFromRetrieveSubscriptionDetail() {
        String subscriptionId = "mockSubscriptionId";
        when(subscriptionRepository.findById(anyString()))
                .thenReturn(Optional.empty());
        Throwable thrown = assertThrows(NoSubscriptionException.class, () ->
                this.subscriptionService.retrieveSubscriptionDetail(subscriptionId));

        assertThat(thrown, instanceOf(NoSubscriptionException.class));
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void shouldReturnDataFromRetrieveSubscriptionDetail() throws SubscriptionGenericException, NoSubscriptionException {
        String subscriptionId = "mockSubscriptionId";
        SubscriptionModel modelMock = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        when(subscriptionRepository.findById(anyString()))
                .thenReturn(Optional.of(modelMock));
        SubscriptionDto detail = this.subscriptionService.retrieveSubscriptionDetail(subscriptionId);

        assertEquals(detail.getId(), modelMock.getId());
        assertEquals(detail.getEmail(), modelMock.getEmail());
        assertEquals(detail.isConsent(), modelMock.isConsent());
        assertEquals(detail.getGender(), modelMock.getGender());
        verify(subscriptionRepository).findById(subscriptionId);
    }

    @Test
    void shouldAlSubscriptionReturnSubscriptionGenericException() {
        int page = 0;
        int pageSize = 1;
        when(subscriptionRepository.findAll(any(Pageable.class)))
                .thenThrow(IllegalArgumentException.class);
        Throwable thrown = assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionService.allSubscription(page, pageSize));
        assertThat(thrown, instanceOf(SubscriptionGenericException.class));
        verify(subscriptionRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldAlSubscriptionReturnNoSubscriptionException() throws SubscriptionGenericException, NoSubscriptionException {
        int page = 0;
        int pageSize = 1;
        SubscriptionModel modelMock = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        List<SubscriptionModel> models = Collections.emptyList();
        Page<SubscriptionModel> pageMock = new PageImpl<>(models);

        when(subscriptionRepository.findAll(any(Pageable.class)))
                .thenReturn(pageMock);

        Throwable thrown = assertThrows(NoSubscriptionException.class, () ->
                this.subscriptionService.allSubscription(page, pageSize));
        assertThat(thrown, instanceOf(NoSubscriptionException.class));
        verify(subscriptionRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnDataFromAlSubscription() throws SubscriptionGenericException, NoSubscriptionException {
        int page = 0;
        int pageSize = 1;
        SubscriptionModel modelMock = SubscriptionModel.builder()
                .id("id")
                .email("email@mail.com")
                .isConsent(true)
                .gender(GenderEnum.NOT_INDICATE)
                .build();
        List<SubscriptionModel> models = Arrays.asList(modelMock,modelMock);
        Page<SubscriptionModel> pageMock = new PageImpl<>(models);

        when(subscriptionRepository.findAll(any(Pageable.class)))
                .thenReturn(pageMock);

        List<SubscriptionDto> result = this.subscriptionService.allSubscription(page, pageSize);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
        verify(subscriptionRepository).findAll(any(Pageable.class));
    }
}