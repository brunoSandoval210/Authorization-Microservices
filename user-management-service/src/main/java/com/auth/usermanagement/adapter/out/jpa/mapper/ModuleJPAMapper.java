package com.auth.usermanagement.adapter.out.jpa.mapper;

import com.auth.usermanagement.adapter.out.jpa.entity.Module;
import com.auth.usermanagement.domain.model.module.ModuleDomain;
import com.auth.usermanagement.domain.model.module.vo.*;
import com.common.shared.domain.exception.MappingException;

public class ModuleJPAMapper {
    public static ModuleDomain toDomain(Module entity) {
        String idStr = entity == null || entity.getModuleId() == null ? "-" : entity.getModuleId().toString();
        try {
            if (entity == null)
                return null;
            var id = new ModuleId(entity.getModuleId());
            var name = entity.getName() == null ? null : new ModuleName(entity.getName());
            var path = entity.getPath() == null ? null : new ModulePath(entity.getPath());
            var icon = entity.getIcon() == null ? null : new ModuleIcon(entity.getIcon());

            return new ModuleDomain(id, name, path, icon, entity.getStatus());
        } catch (Exception e) {
            throw new MappingException("Error mapping Module entity to domain. id=" + idStr, e);
        }
    }

    public static Module fromDomain(ModuleDomain domain) {
        String idStr = domain == null || domain.getModuleId() == null ? "-" : domain.getModuleId().id().toString();
        try {
            if (domain == null)
                return null;
            Module entity = new Module();
            entity.setModuleId(domain.getModuleId().id());
            entity.setName(domain.getName() == null ? null : domain.getName().name());
            entity.setPath(domain.getPath() == null ? null : domain.getPath().url());
            entity.setIcon(domain.getIcon() == null ? null : domain.getIcon().icon());
            entity.setStatus(domain.getStatus());
            return entity;
        } catch (Exception e) {
            throw new MappingException("Error mapping Module domain to entity. id=" + idStr, e);
        }
    }
}
