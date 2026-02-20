package com.common.shared.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ErrorLog(
        String serviceName,
        String errorMessage,
        String stackTrace,
        LocalDateTime timestamp,
        String severity,
        String userId) implements Serializable {

}
