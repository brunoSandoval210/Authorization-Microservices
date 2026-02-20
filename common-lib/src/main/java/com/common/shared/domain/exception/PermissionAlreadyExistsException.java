package com.common.shared.domain.exception;

public class PermissionAlreadyExistsException extends RuntimeException {
    public PermissionAlreadyExistsException(String message) {
        super(message);
    }
}
