package com.audit.domain.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogControlDomain implements Serializable {
    private Long id;
    private LocalDate logDate;
    private String filePath;
    private LocalDateTime createdAt;

    public LogControlDomain() {
    }

    public LogControlDomain(Long id, LocalDate logDate, String filePath, LocalDateTime createdAt) {
        this.id = id;
        this.logDate = logDate;
        this.filePath = filePath;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
