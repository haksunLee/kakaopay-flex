package com.kakaopay.flex.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kakaopay.flex.exception.entity.CustomFieldError;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponseDto {
    private int status;
    private String code;
    private String message;

    //@Valid의 Parameter 검증을 통과하지 못한 필드가 담긴다.
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("errors")
    private List<CustomFieldError> customFieldErrors;

    @Builder
    public ErrorResponseDto(int status, String code, String message, List<CustomFieldError> customFieldErrors) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.customFieldErrors = customFieldErrors;
    }
}
