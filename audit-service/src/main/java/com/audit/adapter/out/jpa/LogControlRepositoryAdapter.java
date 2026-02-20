package com.audit.adapter.out.jpa;

import com.audit.adapter.out.jpa.entity.LogControlEntity;
import com.audit.adapter.out.jpa.repository.LogControlJPARepository;
import com.audit.domain.model.LogControlDomain;
import com.audit.domain.port.out.LogControlRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogControlRepositoryAdapter implements LogControlRepositoryPort {

    private final LogControlJPARepository logControlJPARepository;

    @Override
    public Optional<LogControlDomain> findByDate(LocalDate date) {
        return logControlJPARepository.findByLogDate(date)
                .map(this::toDomain);
    }

    @Override
    public void save(LogControlDomain logControl) {
        LogControlEntity entity = LogControlEntity.builder()
                .id(logControl.getId())
                .logDate(logControl.getLogDate())
                .filePath(logControl.getFilePath())
                .createdAt(logControl.getCreatedAt())
                .build();
        logControlJPARepository.save(entity);
    }

    private LogControlDomain toDomain(LogControlEntity entity) {
        LogControlDomain domain = new LogControlDomain();
        domain.setId(entity.getId());
        domain.setLogDate(entity.getLogDate());
        domain.setFilePath(entity.getFilePath());
        domain.setCreatedAt(entity.getCreatedAt());

        return domain;
    }
}
