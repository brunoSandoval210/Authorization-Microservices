package com.common.shared.domain.exception;

public class PermissionNotAssignedException extends RuntimeException {
    public PermissionNotAssignedException(String message) {
        super(message);
    }
}
