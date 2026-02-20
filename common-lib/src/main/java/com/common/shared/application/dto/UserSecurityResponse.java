package com.common.shared.application.dto;

import java.util.List;

public record UserSecurityResponse(
        String userId,
        String email,
        String password,
        boolean enabled,
        List<String> roles,
        List<String> permissions) {
}
