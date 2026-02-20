package com.auth.usermanagement.adapter.in.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(
                @NotBlank(message = "El nombre del rol es obligatorio") @Size(max = 100) String name,
                @Size(max = 200) String description) {
}
