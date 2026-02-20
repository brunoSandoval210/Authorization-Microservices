package com.auth.usermanagement.domain.model.user.vo;

import java.util.UUID;

import com.common.shared.domain.exception.NullValueException;

public record UserId(UUID id) {
    public UserId {
        if (id == null) {
            throw new NullValueException("del user id");
        }
    }
}
