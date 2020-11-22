package com.kakaopay.flex.exception;

import com.kakaopay.flex.constants.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class InternalServerException extends FlexException {

    private static final long serialVersionUID = -2116671122895194101L;

    private final Errors errors;

    public InternalServerException(Errors errors) {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
        this.errors = errors;
    }
}