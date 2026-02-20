package com.common.shared.security.service;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPrincipal implements Principal {
    private String username;
    private String userId;

    @Override
    public String getName() {
        return username;
    }
}
