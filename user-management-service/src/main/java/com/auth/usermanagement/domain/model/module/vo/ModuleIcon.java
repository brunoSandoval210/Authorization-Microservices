package com.auth.usermanagement.domain.model.module.vo;

import com.common.shared.domain.exception.NullValueException;

public record ModuleIcon(String icon) {
    public ModuleIcon {
        if (icon == null) {
            throw new NullValueException("moduleIcon");
        }
        icon = icon.trim();
        if (icon.isEmpty()) {
            throw new IllegalArgumentException("El icono del modulo no puede estar vacío");
        }
    }
}
