package com.auth.usermanagement.domain.port.in;

import com.common.shared.application.dto.UserSecurityResponse;

public interface GetUserForAuthUseCase {
    UserSecurityResponse execute(String email);
}
