package com.adidas.subscription.controller;

import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.exception.DownstreamServicesException;
import com.adidas.subscription.exception.FeignExceptionHandler;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestControllerAdvice
public class CustomRestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    private final FeignExceptionHandler feignExceptionHandler;

    @Autowired
    public CustomRestExceptionHandler(FeignExceptionHandler feignExceptionHandler) {
        this.feignExceptionHandler = feignExceptionHandler;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
    ) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<String> errors = new ArrayList<>(fieldErrors.size() + globalErrors.size());
        String error;
        for (FieldError fieldError : fieldErrors) {
            error = fieldError.getField() + ", " + fieldError.getDefaultMessage();
            errors.add(error);
        }
        for (ObjectError objectError : globalErrors) {
            error = objectError.getObjectName() + ", " + objectError.getDefaultMessage();
            errors.add(error);
        }
        logger.error("Error:{} ", errors, ex);
        return new ResponseEntity<>(ErrorInfo.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> handleConstraintViolatedException(ConstraintViolationException ex
    ) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        List<String> errors = new ArrayList<>(constraintViolations.size());
        String error;
        for (ConstraintViolation constraintViolation : constraintViolations) {

            error = constraintViolation.getMessage();
            errors.add(error);
        }
        logger.error("Error:{} ", errors, ex);
        return new ResponseEntity<>(ErrorInfo.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex
    ) {
        List<String> errors = new ArrayList<>();
        String error = ex.getParameterName() + ", " + ex.getMessage();
        errors.add(error);
        logger.error("Error:{} ", errors, ex);
        return new ResponseEntity<>(ErrorInfo.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorInfo> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex
    ) {
        String unsupported = "Unsupported content type: " + ex.getContentType();
        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        logger.error("Error:{} ", ex.getMessage(), ex);
        return new ResponseEntity<>(ErrorInfo.builder().message(String.format("unsupported:%s  ,  supported:%s", unsupported, supported)).build()
                , HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        String exceptionName = mostSpecificCause.getClass().getName();
        String message = mostSpecificCause.getMessage();
        return new ResponseEntity<>(ErrorInfo.builder().message(String.format(":%s  :  :%s", exceptionName, message)).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DownstreamServicesException.class, SubscriptionGenericException.class})
    @ResponseBody
    public ResponseEntity<ErrorInfo> customExceptionHandler(Exception ex, HttpServletRequest servletRequest) {
        ErrorInfo error = null;
        if (ex instanceof DownstreamServicesException) {
            error = ((DownstreamServicesException) ex).getError();
        } else if (ex instanceof SubscriptionGenericException) {
            error = ((SubscriptionGenericException) ex).getError();
        }
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSubscriptionException.class})
    @ResponseBody
    public ResponseEntity<ErrorInfo> noSubscriptionExceptionHandler(NoSubscriptionException ex, HttpServletRequest servletRequest) {
        return new ResponseEntity<>(ex.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({FeignException.class})
    @ResponseBody
    public ResponseEntity<ErrorInfo> feignExceptionHandler(FeignException ex, HttpServletRequest servletRequest) {
        ErrorInfo error = this.feignExceptionHandler.processFeignException(ex).orElse(ErrorInfo.builder().build());
        HttpStatus httpStatus = Optional.ofNullable(HttpStatus.resolve(ex.status())).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        logger.error("Error:{} ", ex.getMessage(), ex);
        return new ResponseEntity<>(error, httpStatus);
    }
}
