package com.adidas.subscription.controller;

import com.adidas.subscription.dto.SubscriptionDto;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import com.adidas.subscription.service.SubscriptionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    private SubscriptionController subscriptionController;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        this.subscriptionService = mock(SubscriptionService.class);
        this.subscriptionController = new SubscriptionController(subscriptionService);
    }

    @Test
    void shouldRegisterSubscriptionHandlerSubscriptionGenericException() throws Throwable {
        SubscriptionDto newSubscriptionDto = SubscriptionDto.builder()
                .email("email@mail.com")
                .firstName("test")
                .build();

        when(subscriptionService.registerSubscription(any(SubscriptionDto.class)))
                .thenThrow(SubscriptionGenericException.class);

        Throwable thrown = assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionController.registerSubscription(newSubscriptionDto));

        assertThat(thrown, instanceOf(SubscriptionGenericException.class));
        verify(subscriptionService).registerSubscription(newSubscriptionDto);
    }

    @Test
    void shouldReturnResultWhenProcessRegisterSubscription() throws Throwable {
        SubscriptionDto newSubscriptionDto = SubscriptionDto.builder()
                .email("email@mail.com")
                .firstName("test")
                .build();
        SubscriptionDto savedSubsDtoMock = SubscriptionDto.builder()
                .id("MockID")
                .email("email@mail.com")
                .firstName("test")
                .build();
        when(subscriptionService.registerSubscription(any(SubscriptionDto.class)))
                .thenReturn(savedSubsDtoMock);

        SubscriptionDto savedSubsDto = this.subscriptionController.registerSubscription(newSubscriptionDto);
        assertThat(savedSubsDto, is(notNullValue()));
        assertThat(savedSubsDto, equalTo(savedSubsDtoMock));
        verify(subscriptionService).registerSubscription(newSubscriptionDto);
    }

    @Test
    void shouldRemoveSubscriptionHandlerSubscriptionGenericException() throws Throwable {
        String subscriptionId = "mockSubscriptionId";
        doThrow(SubscriptionGenericException.class)
                .when(subscriptionService).removeSubscription(anyString());
        assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionController.removeSubscription(subscriptionId));
        verify(subscriptionService).removeSubscription(subscriptionId);
    }

    @Test
    void shouldRemoveSubscriptionBySubscriptionId() throws Throwable {
        String subscriptionId = "mockSubscriptionId";
        doNothing().when(subscriptionService).removeSubscription(anyString());
        assertDoesNotThrow( () -> this.subscriptionController.removeSubscription(subscriptionId));
        verify(subscriptionService).removeSubscription(subscriptionId);
    }

    @Test
    void shouldDetailSubscriptionHandlerSubscriptionGenericException() throws SubscriptionGenericException, NoSubscriptionException {
        String subscriptionId = "mockSubscriptionId";
        when(subscriptionService.retrieveSubscriptionDetail(anyString()))
                .thenThrow(SubscriptionGenericException.class);
        assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionController.detailSubscription(subscriptionId));
        verify(subscriptionService).retrieveSubscriptionDetail(subscriptionId);
    }

    @Test
    void shouldReturnSubscriptionFromDetailSubscription() throws Throwable {
        String subscriptionId = "mockSubscriptionId";
        SubscriptionDto returnSubsDtoMock = SubscriptionDto.builder()
                .id("MockID")
                .email("email@mail.com")
                .firstName("test")
                .build();
        when(subscriptionService.retrieveSubscriptionDetail(anyString()))
                .thenReturn(returnSubsDtoMock);
        SubscriptionDto result = this.subscriptionController.detailSubscription(subscriptionId);
        assertThat(result, is(notNullValue()));
        assertThat(result, equalTo(returnSubsDtoMock));
        verify(subscriptionService).retrieveSubscriptionDetail(subscriptionId);
    }

    @Test
    void shouldAllSubscriptionHandlerSubscriptionGenericException() throws SubscriptionGenericException, NoSubscriptionException {
        int page = 1;
        int pageSize = 1;

        when(subscriptionService.allSubscription(anyInt(),anyInt()))
                .thenThrow(SubscriptionGenericException.class);

        assertThrows(SubscriptionGenericException.class, () ->
                this.subscriptionController.allSubscription(page, pageSize));

        verify(subscriptionService).allSubscription(page, pageSize);
    }

    @Test
    void shouldReturnListOfSubscriptionFromAllSubscription() throws Throwable {
        int page = 1;
        int pageSize = 1;
        SubscriptionDto returnSubsDtoMock = SubscriptionDto.builder()
                .id("MockID")
                .email("email@mail.com")
                .firstName("test")
                .build();
        when(subscriptionService.allSubscription(anyInt(),anyInt()))
                .thenReturn(Arrays.asList(returnSubsDtoMock,returnSubsDtoMock));

        List<SubscriptionDto> result = this.subscriptionController.allSubscription(page, pageSize);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(2));
        verify(subscriptionService).allSubscription(page, pageSize);
    }
}