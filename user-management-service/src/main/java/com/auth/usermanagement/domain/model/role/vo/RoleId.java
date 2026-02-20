package com.auth.usermanagement.domain.model.role.vo;

import java.util.UUID;

import com.common.shared.domain.exception.NullValueException;

public record RoleId(UUID id) {
    public RoleId {
        if (id == null) {
            throw new NullValueException("roleId");
        }
    }
}
