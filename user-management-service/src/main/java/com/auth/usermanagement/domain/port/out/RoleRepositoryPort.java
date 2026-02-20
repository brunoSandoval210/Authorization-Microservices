package com.auth.usermanagement.domain.port.out;

import com.auth.usermanagement.domain.model.role.RoleDomain;
import com.auth.usermanagement.domain.model.role.vo.RoleId;
import com.auth.usermanagement.domain.model.role.vo.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryPort {
    RoleDomain save(RoleDomain domain);

    Optional<RoleDomain> findById(RoleId id);

    Optional<RoleDomain> findByName(RoleName name);

    List<RoleDomain> findAll();

    boolean existsById(RoleId id);

    void deleteById(RoleId id);

    void updateEnabled(RoleId id, boolean enabled);

    Page<RoleDomain> searchByName(String name, Pageable pageable);
}
