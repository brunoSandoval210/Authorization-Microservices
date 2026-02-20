package com.auth.usermanagement.domain.port.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auth.usermanagement.adapter.in.web.request.CreatePermissionRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdatePermissionRequest;
import com.auth.usermanagement.application.dto.out.PermissionResponse;
import com.common.shared.application.dto.PaginatedResponse;

public interface PermissionUseCasePort {
    PermissionResponse create(CreatePermissionRequest request);

    PermissionResponse update(UUID id, UpdatePermissionRequest request);

    void deactivate(UUID id);

    void activate(UUID id);

    Optional<PermissionResponse> findById(UUID id);

    List<PermissionResponse> findAll();

    PaginatedResponse<PermissionResponse> search(String name, int page, int size);
}
