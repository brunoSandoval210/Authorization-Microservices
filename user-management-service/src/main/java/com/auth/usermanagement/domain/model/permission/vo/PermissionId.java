package com.auth.usermanagement.domain.model.permission.vo;

import java.util.UUID;

import com.common.shared.domain.exception.NullValueException;

public record PermissionId(UUID id) {
    public PermissionId {
        if (id == null) {
            throw new NullValueException("permissionId");
        }
    }
}
