package com.auth.usermanagement.adapter.out.jpa.mapper;

import com.auth.usermanagement.adapter.out.jpa.entity.Permission;
import com.auth.usermanagement.adapter.out.jpa.entity.Role;
import com.auth.usermanagement.domain.model.permission.PermissionDomain;
import com.auth.usermanagement.domain.model.role.RoleDomain;
import com.auth.usermanagement.domain.model.role.vo.RoleDescription;
import com.auth.usermanagement.domain.model.role.vo.RoleId;
import com.auth.usermanagement.domain.model.role.vo.RoleName;
import com.common.shared.domain.exception.MappingException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleJPAMapper {
    public static RoleDomain toDomain(Role entity) {
        String idStr = entity == null || entity.getRoleId() == null ? "-" : entity.getRoleId().toString();
        try {
            if (entity == null)
                return null;
            RoleId id = new RoleId(entity.getRoleId());
            RoleName name = entity.getName() == null ? null : new RoleName(entity.getName());
            RoleDescription desc = entity.getDescription() == null ? null
                    : new RoleDescription(entity.getDescription());

            Set<PermissionDomain> permissions = entity.getPermissions() == null
                    ? new HashSet<>()
                    : entity.getPermissions().stream()
                            .map(PermissionJPAMapper::toDomain)
                            .collect(Collectors.toSet());

            return new RoleDomain(id, name, desc, permissions, entity.getStatus());
        } catch (Exception e) {
            throw new MappingException("Error al transformar Role entity a domain. id=" + idStr, e);
        }
    }

    public static Role fromDomain(RoleDomain domain) {
        String idStr = domain == null || domain.getRoleId() == null ? "-" : domain.getRoleId().id().toString();
        try {
            if (domain == null)
                return null;

            Role entity = new Role();
            entity.setRoleId(domain.getRoleId().id());
            entity.setName(domain.getName() == null ? null : domain.getName().value());
            entity.setDescription(domain.getDescription() == null ? null : domain.getDescription().description());

            // Mapeo de IDs de Permisos para persistencia
            if (domain.getPermissions() != null) {
                entity.setPermissions(domain.getPermissions().stream()
                        .map(permDomain -> {
                            Permission permEntity = new Permission();
                            permEntity.setPermissionId(permDomain.getPermissionId().id());
                            return permEntity;
                        })
                        .collect(Collectors.toSet()));
            }
            entity.setStatus(domain.getStatus());

            return entity;
        } catch (Exception e) {
            throw new MappingException("Error al transformar Role domain a entity. id=" + idStr, e);
        }
    }
}
