package com.common.shared.application.dto;

import java.time.LocalDateTime;

public record AuditMessage(
                String serviceName,
                String action,
                String userId,
                String details,
                LocalDateTime timestamp,
                String status,
                String traceId) {
}
