package com.auth.usermanagement.domain.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth.usermanagement.domain.model.module.ModuleDomain;
import com.auth.usermanagement.domain.model.module.vo.ModuleId;

import java.util.List;
import java.util.Optional;

public interface ModuleRepositoryPort {
    ModuleDomain save(ModuleDomain module);

    Optional<ModuleDomain> findById(ModuleId id);

    List<ModuleDomain> findAll();

    void deleteById(ModuleId id);

    void updateEnabled(ModuleId id, boolean enabled);

    Page<ModuleDomain> searchByName(String name, Pageable pageable);
}
