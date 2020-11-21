package com.kakaopay.flex.exception.entity;

import lombok.Builder;

public class CustomFieldError {
    private final String field;
    private final Object value;
    private final String reason;

    @Builder
    public CustomFieldError(String field, Object value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }
}