package com.auth.usermanagement.application.services.mapper;

import com.auth.usermanagement.adapter.in.web.request.CreateRoleRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdateRoleRequest;
import com.auth.usermanagement.application.dto.out.PermissionResponse;
import com.auth.usermanagement.application.dto.out.RoleResponse;
import com.auth.usermanagement.domain.model.role.RoleDomain;
import com.auth.usermanagement.domain.model.role.vo.RoleDescription;
import com.auth.usermanagement.domain.model.role.vo.RoleId;
import com.auth.usermanagement.domain.model.role.vo.RoleName;
import com.common.shared.domain.exception.MappingException;
import com.common.shared.domain.model.Status;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RoleMapper {

    public static RoleDomain toDomain(CreateRoleRequest request) {
        try {
            return new RoleDomain(
                    new RoleId(UUID.randomUUID()),
                    request.name() == null ? null : new RoleName(request.name()),
                    request.description() == null ? null : new RoleDescription(request.description()),
                    null,
                    Status.ACTIVO);
        } catch (Exception e) {
            throw new MappingException("Error mapeando CreateRoleRequest a RoleDomain", e);
        }
    }

    public static void applyUpdate(RoleDomain existing, UpdateRoleRequest req) {
        try {
            if (existing == null || req == null)
                return;
            if (req.name() != null) {
                existing.updateName(new RoleName(req.name()));
            }
            if (req.description() != null) {
                existing.updateDescription(new RoleDescription(req.description()));
            }
        } catch (Exception e) {
            throw new MappingException("Error aplicando UpdateRoleRequest a RoleDomain", e);
        }
    }

    public static RoleResponse toResponse(RoleDomain domain) {
        try {
            if (domain == null)
                return null;
            String name = domain.getName() == null ? null : domain.getName().value();
            String desc = domain.getDescription() == null ? null : domain.getDescription().description();
            List<PermissionResponse> perms = domain.getPermissions() == null ? List.of()
                    : domain.getPermissions().stream()
                            .map(p -> new PermissionResponse(
                                    p.getPermissionId().id(),
                                    p.getName() == null ? null : p.getName().value(),
                                    p.getDescription() == null ? null : p.getDescription().description(),
                                    p.getStatus().name()))
                            .collect(Collectors.toList());
            String status = domain.getStatus() == null ? null : domain.getStatus().name();

            return new RoleResponse(domain.getRoleId().id(), name, desc, perms, status);
        } catch (Exception e) {
            throw new MappingException("Error mapeando RoleDomain a RoleResponse", e);
        }
    }
}
