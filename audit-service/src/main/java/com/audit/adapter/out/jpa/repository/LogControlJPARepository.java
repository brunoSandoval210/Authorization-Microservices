package com.audit.adapter.out.jpa.repository;

import com.audit.adapter.out.jpa.entity.LogControlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LogControlJPARepository extends JpaRepository<LogControlEntity, Long> {
    Optional<LogControlEntity> findByLogDate(LocalDate logDate);
}
