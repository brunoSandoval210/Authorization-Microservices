package com.auth.usermanagement.domain.model.user.vo;

import com.common.shared.domain.model.Status;

public record AccountStatus(
        boolean isEnabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        Status status) {
}
