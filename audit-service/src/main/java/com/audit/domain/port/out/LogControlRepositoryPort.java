package com.audit.domain.port.out;

import com.audit.domain.model.LogControlDomain;

import java.time.LocalDate;
import java.util.Optional;

public interface LogControlRepositoryPort {
    Optional<LogControlDomain> findByDate(LocalDate date);

    void save(LogControlDomain logControl);
}
