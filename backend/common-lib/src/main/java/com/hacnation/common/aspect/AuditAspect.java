package com.hacnation.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.events.AuditEvent;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Aspect
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    private final String serviceName;
    private final ObjectMapper objectMapper;

    public AuditAspect(KafkaTemplate<String, AuditEvent> kafkaTemplate, String serviceName,
                       ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.serviceName = serviceName;
        this.objectMapper = objectMapper;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *) || within(@org.springframework.stereotype.Controller *)")
    public Object auditRestEndpoint(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestUri = getCurrentRequestUri();
        boolean isHealthEndpoint = requestUri != null && requestUri.contains("/actuator/");

        if (isHealthEndpoint) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String action = extractAction(signature);

        try {
            Object result = joinPoint.proceed();

            String userId = extractUserId();
            String entityId = extractEntityId(joinPoint);
            String newValue = safeSerialize(result);

            publishAudit(userId, action, null, newValue, entityId);

            return result;
        } catch (Throwable t) {
            log.debug("Audit skip pour exception: {}", t.getMessage());
            throw t;
        }
    }

    private String extractUserId() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String userId = request.getHeader("X-User-Id");
                return userId != null ? userId : "anonymous";
            }
        } catch (Exception ignored) {
        }
        return "anonymous";
    }

    private String extractAction(MethodSignature signature) {
        String methodName = signature.getName();
        String path = getCurrentRequestUri();
        return (path != null ? path : "") + "|" + methodName;
    }

    private String extractEntityId(ProceedingJoinPoint joinPoint) {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof String) {
                return (String) arg;
            }
        }
        return "N/A";
    }

    private String getCurrentRequestUri() {
        try {
            return ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        } catch (Exception ignored) {
            return null;
        }
    }

    private String safeSerialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "[non-serializable]";
        }
    }

    private void publishAudit(String userId, String action, String oldValue, String newValue, String entityId) {
        try {
            AuditEvent event = new AuditEvent(
                    userId,
                    LocalDateTime.now(),
                    serviceName,
                    action,
                    oldValue,
                    newValue,
                    entityId,
                    UUID.randomUUID().toString()
            );
            kafkaTemplate.send("sic.audit", entityId, event);
        } catch (Exception e) {
            log.error("Echec publication audit Kafka (non-bloquant): {}", e.getMessage());
        }
    }
}
