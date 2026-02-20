package com.auth.gatewayservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;


@Configuration
public class GatewayRoutesConfig {

    @Value("${application.services.auth-service.url:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${application.services.user-service.url:http://localhost:8082}")
    private String userServiceUrl;

    @Value("${application.services.audit-service.url:http://localhost:8083}")
    private String auditServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute() {
        return route("auth_service")
                .route(path("/api/auth/**"), http())
                .before(uri(URI.create(authServiceUrl)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service")
                .route(path("/api/users/**"), http())
                .before(uri(URI.create(userServiceUrl)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> auditServiceRoute() {
        return route("audit_service")
                .route(path("/api/audit-logs/**"), http())
                .before(uri(URI.create(auditServiceUrl)))
                .build();
    }
}
