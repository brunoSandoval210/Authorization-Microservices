package com.audit.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActivityLogDomain {

    private UUID id;
    private String userId;
    private String module;
    private String action;
    private String details;
    private String ipAddress;
    private String status;
    private LocalDateTime timestamp;

    public ActivityLogDomain() {
    }

    public ActivityLogDomain(UUID id, String userId, String module, String action, String details, String ipAddress,
            String status, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.module = module;
        this.action = action;
        this.details = details;
        this.ipAddress = ipAddress;
        this.status = status;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
