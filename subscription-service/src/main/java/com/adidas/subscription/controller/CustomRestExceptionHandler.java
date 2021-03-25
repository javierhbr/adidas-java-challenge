package com.adidas.subscription.controller;


import com.adidas.subscription.dto.ErrorInfo;
import com.adidas.subscription.exception.NoSubscriptionException;
import com.adidas.subscription.exception.SubscriptionGenericException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class CustomRestExceptionHandler{
    private static final Logger logger = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

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
        return new ResponseEntity<>(ErrorInfo.builder().errors(errors).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ErrorInfo> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex
    ) {
        String unsupported = "Unsupported content type: " + ex.getContentType();
        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());
        return new ResponseEntity<>(ErrorInfo.builder().message(String.format("unsupported:%s  ,  supported:%s",unsupported, supported)).build()
                , HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorInfo> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
            String exceptionName = mostSpecificCause.getClass().getName();
            String message = mostSpecificCause.getMessage();
        logger.error("Error:{} ", message, ex);
        return new ResponseEntity<>(ErrorInfo.builder().message(String.format(":%s  :  :%s",exceptionName, message)).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SubscriptionGenericException.class})
    @ResponseBody
    public ResponseEntity<ErrorInfo> customExceptionHandler(SubscriptionGenericException ex) {
        ErrorInfo error = ex.getError();
        logger.error("Error:{} ", error, ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ NoSubscriptionException.class})
    @ResponseBody
    public ResponseEntity<ErrorInfo> noSubscriptionExceptionHandler(NoSubscriptionException ex) {
        logger.error("Error:{} ", ex);
        return new ResponseEntity<>(ex.getError(), HttpStatus.NOT_FOUND);
    }
}
