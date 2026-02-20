package com.audit.adapter.out.jpa.repository;

import com.audit.adapter.out.jpa.entity.ActivityLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AuditLogJPARepository extends JpaRepository<ActivityLogEntity, UUID> {
    Page<ActivityLogEntity> findAllByModule(String module, Pageable pageable);

    Page<ActivityLogEntity> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<ActivityLogEntity> findAllByModuleAndTimestampBetween(String module, LocalDateTime start, LocalDateTime end,
                                                               Pageable pageable);
}
