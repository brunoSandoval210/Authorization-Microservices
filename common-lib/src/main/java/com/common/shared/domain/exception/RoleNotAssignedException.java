package com.common.shared.domain.exception;

public class RoleNotAssignedException extends RuntimeException {
    public RoleNotAssignedException(String message) {
        super(message);
    }
}
