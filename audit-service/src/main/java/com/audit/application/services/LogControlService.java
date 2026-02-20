package com.audit.application.services;

import com.audit.domain.model.LogControlDomain;
import com.audit.domain.port.in.LogControlUseCasePort;
import com.audit.domain.port.out.LogControlRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogControlService implements LogControlUseCasePort {

    private final LogControlRepositoryPort logControlRepositoryPort;

    @Override
    @Transactional
    public void ensureLogControlExists(LocalDate date, String filePath) {
        if (logControlRepositoryPort.findByDate(date).isEmpty()) {
            LogControlDomain logControl = new LogControlDomain();
            logControl.setLogDate(date);
            logControl.setFilePath(filePath);
            logControl.setCreatedAt(LocalDateTime.now());

            logControlRepositoryPort.save(logControl);
        }
    }
}
