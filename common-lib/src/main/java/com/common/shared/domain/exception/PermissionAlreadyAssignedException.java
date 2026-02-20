package com.common.shared.domain.exception;

public class PermissionAlreadyAssignedException extends RuntimeException {
    public PermissionAlreadyAssignedException(String message) {
        super(message);
    }
}
