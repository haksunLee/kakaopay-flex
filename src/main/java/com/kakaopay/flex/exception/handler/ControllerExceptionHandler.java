package com.kakaopay.flex.exception.handler;

import com.kakaopay.flex.exception.FlexException;
import com.kakaopay.flex.exception.InternalServerException;
import com.kakaopay.flex.exception.dto.ErrorResponseDto;
import com.kakaopay.flex.exception.entity.CustomFieldError;
import com.kakaopay.flex.web.FlexResponse;
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
    protected ResponseEntity<FlexResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.ok(new FlexResponse(responseDto));
    }

    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<FlexResponse>  handleInvalidParameterException(InternalServerException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
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
        return ResponseEntity.ok(new FlexResponse(responseDto));
    }

    @ExceptionHandler(FlexException.class)
    protected ResponseEntity<FlexResponse> handleFlexApiException(FlexException e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.ok(new FlexResponse(responseDto));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<FlexResponse> handleException(Exception e) {

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.toString())
                .build();
        return ResponseEntity.ok(new FlexResponse(responseDto));
    }
}
