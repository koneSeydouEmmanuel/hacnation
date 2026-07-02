package com.hacnation.gateway.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class KeycloakJwtFilter implements GlobalFilter, Ordered {

    private static final Set<String> PUBLIC_PATHS = new HashSet<>(Arrays.asList(
            "/api/auth/",
            "/actuator/"
    ));

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        String traceId = UUID.randomUUID().toString();
        ServerHttpRequest requestWithTraceId = exchange.getRequest().mutate()
                .header("X-Trace-Id", traceId)
                .build();
        ServerWebExchange enrichedExchange = exchange.mutate().request(requestWithTraceId).build();

        if (isPublicPath(path)) {
            return chain.filter(enrichedExchange);
        }

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                        ServerHttpRequest modifiedRequest = enrichRequest(enrichedExchange, jwt);
                        return chain.filter(enrichedExchange.mutate().request(modifiedRequest).build());
                    }
                    return chain.filter(enrichedExchange);
                })
                .switchIfEmpty(chain.filter(enrichedExchange));
    }

    private ServerHttpRequest enrichRequest(ServerWebExchange exchange, Jwt jwt) {
        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("preferred_username");
        List<String> roles = extractRoles(jwt);
        String email = jwt.getClaimAsString("email");
        String fullName = jwt.getClaimAsString("name");

        return exchange.getRequest().mutate()
                .header("X-User-Id", userId != null ? userId : "")
                .header("X-Username", username != null ? username : "")
                .header("X-User-Roles", roles != null ? String.join(",", roles) : "")
                .header("X-User-Email", email != null ? email : "")
                .header("X-User-Name", fullName != null ? fullName : "")
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return List.of();
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
