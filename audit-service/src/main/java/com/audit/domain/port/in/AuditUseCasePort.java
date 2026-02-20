package com.audit.domain.port.in;

import org.springframework.data.domain.Pageable;

import com.audit.domain.model.ActivityLogDomain;
import com.common.shared.application.dto.PaginatedResponse;

import java.time.LocalDate;

public interface AuditUseCasePort {
    void logActivity(ActivityLogDomain activityLog);

    PaginatedResponse<ActivityLogDomain> retrieveLogs(String module, LocalDate date, Pageable pageable);
}
