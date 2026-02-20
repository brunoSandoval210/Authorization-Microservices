package com.auth.authservice.domain.port.in;

import com.common.shared.application.dto.UserSecurityResponse;

public interface GetUserForAuthUseCase {
    UserSecurityResponse execute(String email);
}
