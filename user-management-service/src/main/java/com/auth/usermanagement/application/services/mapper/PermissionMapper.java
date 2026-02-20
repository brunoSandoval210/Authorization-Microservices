package com.auth.usermanagement.application.services.mapper;

import java.util.UUID;

import com.auth.usermanagement.adapter.in.web.request.CreatePermissionRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdatePermissionRequest;
import com.auth.usermanagement.application.dto.out.PermissionResponse;
import com.auth.usermanagement.domain.model.permission.PermissionDomain;
import com.auth.usermanagement.domain.model.permission.vo.PermissionDescription;
import com.auth.usermanagement.domain.model.permission.vo.PermissionId;
import com.auth.usermanagement.domain.model.permission.vo.PermissionName;
import com.common.shared.domain.exception.MappingException;
import com.common.shared.domain.model.Status;

public class PermissionMapper {
    public static PermissionDomain toDomain(CreatePermissionRequest req) {
        try {
            return new PermissionDomain(
                    new PermissionId(UUID.randomUUID()),
                    req.name() == null ? null : new PermissionName(req.name()),
                    req.description() == null ? null : new PermissionDescription(req.description()),
                    null,
                    Status.ACTIVO);
        } catch (Exception e) {
            throw new MappingException("Error mapeando CreatePermissionRequest a PermissionDomain", e);
        }
    }

    public static void applyUpdate(PermissionDomain existing, UpdatePermissionRequest req) {
        try {
            if (existing == null || req == null)
                return;
            if (req.name() != null)
                existing.updateName(new PermissionName(req.name()));
            if (req.description() != null)
                existing.updateDescription(new PermissionDescription(req.description()));
        } catch (Exception e) {
            throw new MappingException("Error aplicando UpdatePermissionRequest a PermissionDomain", e);
        }
    }

    public static PermissionResponse toResponse(PermissionDomain domain) {
        try {
            if (domain == null)
                return null;
            String name = domain.getName() == null ? null : domain.getName().value();
            String desc = domain.getDescription() == null ? null : domain.getDescription().description();
            String status = domain.getStatus() == null ? null : domain.getStatus().name();

            return new PermissionResponse(domain.getPermissionId().id(), name, desc, status);
        } catch (Exception e) {
            throw new MappingException("Error mapeando PermissionDomain a PermissionResponse", e);
        }
    }
}
