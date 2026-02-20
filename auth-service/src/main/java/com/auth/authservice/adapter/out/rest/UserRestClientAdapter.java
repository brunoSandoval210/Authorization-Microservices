package com.auth.authservice.adapter.out.rest;

import com.auth.authservice.domain.port.in.GetUserForAuthUseCase;
import com.common.shared.application.dto.UserSecurityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserRestClientAdapter implements GetUserForAuthUseCase {

    private final RestTemplate restTemplate;

    @Value("${application.services.user-service.url}")
    private String userServiceUrl;

    @Override
    public UserSecurityResponse execute(String email) {
        // userServiceUrl should be http://user-management-service:8082/internal/users
        String url = userServiceUrl + "/search/findByUsername?username=" + email;
        return restTemplate.getForObject(url, UserSecurityResponse.class);
    }
}
