package com.logistics.platform.distribution.user.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String message;
    public BusinessException(String message) {
        this.message = message;
    }
}
