package com.kakaopay.flex.exception.handler;

import com.kakaopay.flex.exception.FlexException;
import com.kakaopay.flex.exception.InvalidParameterException;
import com.kakaopay.flex.exception.dto.ErrorResponseDto;
import com.kakaopay.flex.exception.entity.CustomFieldError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<ErrorResponseDto> handleInvalidParameterException(InvalidParameterException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(e.getErrorCode().getStatus())
                .message(e.toString())
                .customFieldErrors(
                        e.getErrors().getFieldErrors().stream()
                        .map(fieldError -> CustomFieldError.builder()
                                .field(fieldError.getCodes()[0])
                                .value(fieldError.getRejectedValue())
                                .reason(fieldError.getDefaultMessage())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.resolve(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(FlexException.class)
    protected ResponseEntity<ErrorResponseDto> handleFlexApiException(FlexException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(e.getErrorCode().getStatus())
                .code(e.getErrorCode().getCode())
                .message(e.toString())
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.resolve(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString())
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
