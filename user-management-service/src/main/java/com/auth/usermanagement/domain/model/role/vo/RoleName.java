package com.auth.usermanagement.domain.model.role.vo;

public record RoleName(String value) {
    public RoleName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del rol no puede ser nulo o vacío");
        }
        value = value.trim();
    }
}
