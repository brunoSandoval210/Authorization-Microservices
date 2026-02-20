package com.audit.domain.port.in;

import java.time.LocalDate;

public interface LogControlUseCasePort {
    void ensureLogControlExists(LocalDate date, String filePath);
}
