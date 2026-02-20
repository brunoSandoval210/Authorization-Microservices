package com.auth.usermanagement.domain.model.user.vo;

import com.common.shared.domain.exception.WeakPasswordException;

public record UserPassword(String value) {
    public UserPassword {
        if (value == null || value.length() < 8) {
            throw new WeakPasswordException("La contraseña debe tener al menos 8 caracteres");
        }
    }
}
