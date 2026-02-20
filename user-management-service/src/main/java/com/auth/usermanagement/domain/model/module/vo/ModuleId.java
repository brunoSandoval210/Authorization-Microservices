package com.auth.usermanagement.domain.model.module.vo;

import java.util.UUID;

import com.common.shared.domain.exception.NullValueException;

public record ModuleId(UUID id) {
    public ModuleId {
        if (id == null) {
            throw new NullValueException("moduleId");
        }
    }
}
