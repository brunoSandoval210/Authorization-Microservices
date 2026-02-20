package com.auth.usermanagement.domain.model.role.vo;

public record RoleDescription(String description) {
    public RoleDescription {
        if (description != null) {
            String d = description.trim();
            if (d.isEmpty()) {
                description = null;
            } else {
                description = d;
            }
        }
    }
}
