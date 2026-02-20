package com.audit.adapter.out.jpa.mapper;

import com.audit.adapter.out.jpa.entity.ActivityLogEntity;
import com.audit.domain.model.ActivityLogDomain;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public ActivityLogEntity toEntity(ActivityLogDomain domain) {
        if (domain == null)
            return null;

        ActivityLogEntity activityLog = new ActivityLogEntity();
        activityLog.setId(domain.getId());
        activityLog.setUserId(domain.getUserId());
        activityLog.setModule(domain.getModule());
        activityLog.setAction(domain.getAction());
        activityLog.setDetails(domain.getDetails());
        activityLog.setIpAddress(domain.getIpAddress());
        activityLog.setStatus(domain.getStatus());
        activityLog.setTimestamp(domain.getTimestamp());

        return activityLog;
    }

    public ActivityLogDomain toDomain(ActivityLogEntity entity) {
        if (entity == null)
            return null;
        ActivityLogDomain activityLogDomain = new ActivityLogDomain();
        activityLogDomain.setId(entity.getId());
        activityLogDomain.setUserId(entity.getUserId());
        activityLogDomain.setModule(entity.getModule());
        activityLogDomain.setAction(entity.getAction());
        activityLogDomain.setDetails(entity.getDetails());
        activityLogDomain.setIpAddress(entity.getIpAddress());
        activityLogDomain.setStatus(entity.getStatus());
        activityLogDomain.setTimestamp(entity.getTimestamp());
        return activityLogDomain;
    }
}
