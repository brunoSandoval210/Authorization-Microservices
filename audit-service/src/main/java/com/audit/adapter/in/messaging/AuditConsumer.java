package com.audit.adapter.in.messaging;

import com.audit.domain.model.ActivityLogDomain;
import com.audit.domain.port.in.AuditUseCasePort;
import com.audit.domain.port.in.LogControlUseCasePort;
import com.common.shared.application.dto.AuditMessage;
import com.common.shared.application.dto.ErrorLog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuditConsumer {

    private final AuditUseCasePort auditUseCasePort;
    private final LogControlUseCasePort logControlUseCasePort;

    @Bean
    public Consumer<AuditMessage> auditLogConsumer() {
        return message -> {
            log.info("Received audit log: {}", message);
            ActivityLogDomain activityLog = new ActivityLogDomain(
                    null,
                    message.serviceName(),
                    message.action(),
                    message.userId(),
                    message.details(),
                    null,
                    message.status(),
                    message.timestamp());
            auditUseCasePort.logActivity(activityLog);
        };
    }

    @Bean
    public Consumer<ErrorLog> errorLogConsumer() {
        return dto -> {
            log.error("Received error log from {}: {}", dto.serviceName(), dto.errorMessage());

            // 1. Define File Path based on Date
            LocalDate today = LocalDate.now();
            String fileName = "logs/error-log-" + today.toString() + ".txt";
            File file = new File(fileName);

            // 2. Ensure parent directories exist
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            // 3. Write to File (Append)
            try (FileWriter fw = new FileWriter(file, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {

                out.println("Timestamp: " + dto.timestamp());
                out.println("Service: " + dto.serviceName());
                out.println("Message: " + dto.errorMessage());
                out.println("Stacktrace: " + dto.stackTrace());
                out.println("--------------------------------------------------");
            } catch (IOException e) {
                log.error("Failed to write error log to file: {}", fileName, e);
            }

            // 4. Register Log Control
            logControlUseCasePort.ensureLogControlExists(today, file.getAbsolutePath());
        };
    }
}
