package com.auth.usermanagement.domain.port.in;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.auth.usermanagement.adapter.in.web.request.CreateModuleRequest;
import com.auth.usermanagement.adapter.in.web.request.UpdateModuleRequest;
import com.auth.usermanagement.application.dto.out.ModuleResponse;
import com.common.shared.application.dto.PaginatedResponse;

public interface ModuleUseCasePort {
    ModuleResponse create(CreateModuleRequest request);

    ModuleResponse update(UUID id, UpdateModuleRequest request);

    void deactivate(UUID id);

    void activate(UUID id);

    Optional<ModuleResponse> findById(UUID id);

    List<ModuleResponse> findAll();

    PaginatedResponse<ModuleResponse> search(String name, int page, int size);
}
