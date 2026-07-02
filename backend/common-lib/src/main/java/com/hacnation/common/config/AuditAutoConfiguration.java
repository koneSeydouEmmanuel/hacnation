package com.hacnation.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.aspect.AuditAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(name = "audit.enabled", havingValue = "true", matchIfMissing = true)
public class AuditAutoConfiguration {

    @Value("${spring.application.name:hacnation-service}")
    private String serviceName;

    @Bean
    public AuditAspect auditAspect(KafkaTemplate<String, com.hacnation.common.events.AuditEvent> kafkaTemplate,
                                    ObjectMapper objectMapper) {
        return new AuditAspect(kafkaTemplate, serviceName, objectMapper);
    }
}
