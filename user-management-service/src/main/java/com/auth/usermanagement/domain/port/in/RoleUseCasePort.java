package com.auth.usermanagement.domain.port.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auth.usermanagement.adapter.in.web.request.CreateRoleRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdateRoleRequest;
import com.auth.usermanagement.application.dto.out.RoleResponse;
import com.common.shared.application.dto.PaginatedResponse;

public interface RoleUseCasePort {
    RoleResponse create(CreateRoleRequest request);

    RoleResponse update(UUID id, UpdateRoleRequest request);

    void deactivate(UUID id);

    void activate(UUID id);

    Optional<RoleResponse> findById(UUID id);

    List<RoleResponse> findAll();

    void addPermission(UUID roleId, UUID permissionId);

    void removePermission(UUID roleId, UUID permissionId);

    PaginatedResponse<RoleResponse> search(String name, int page, int size);
}
