package com.auth.usermanagement.domain.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.auth.usermanagement.domain.model.permission.PermissionDomain;
import com.auth.usermanagement.domain.model.permission.vo.PermissionId;
import com.auth.usermanagement.domain.model.permission.vo.PermissionName;

import java.util.List;
import java.util.Optional;

public interface PermissionRepositoryPort {
    PermissionDomain save(PermissionDomain permission);

    Optional<PermissionDomain> findById(PermissionId id);

    Optional<PermissionDomain> findByName(PermissionName name);

    List<PermissionDomain> findAll();

    void deleteById(PermissionId id);

    void updateEnabled(PermissionId id, boolean enabled);

    Page<PermissionDomain> searchByName(String name, Pageable pageable);
}
