package com.common.shared.infrastructure.messaging;

import com.common.shared.application.dto.AuditMessage;
import com.common.shared.application.dto.ErrorLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);

    private final StreamBridge streamBridge;

    public MessagePublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendAuditLog(AuditMessage auditMessage) {
        try {
            streamBridge.send("auditLog-out-0", auditMessage);
        } catch (Exception e) {
            log.error("Failed to send audit log, is RabbitMQ running? Error: {}", e.getMessage());
        }
    }

    public void sendErrorLog(ErrorLog errorLogDto) {
        try {
            streamBridge.send("errorLog-out-0", errorLogDto);
        } catch (Exception e) {
            log.error("Failed to send error log, is RabbitMQ running? Error: {}", e.getMessage());
        }
    }
}
