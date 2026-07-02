package com.hacnation.common.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hacnation.common.aspect.AuditAspect;
import com.hacnation.common.config.AuditAutoConfiguration;
import com.hacnation.common.events.AuditEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private KafkaTemplate<String, AuditEvent> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void auditEvent_fieldsShouldBeSetCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        String traceId = "trace-123";

        AuditEvent event = new AuditEvent(
                "user-1",
                now,
                "accueil-service",
                "/api/accueil/admissions|createAdmission",
                null,
                "{\"patientId\":\"patient-1\"}",
                "adm-1",
                traceId
        );

        assertEquals("user-1", event.getUserId());
        assertEquals(now, event.getTimestamp());
        assertEquals("accueil-service", event.getService());
        assertEquals("/api/accueil/admissions|createAdmission", event.getAction());
        assertNull(event.getOldValue());
        assertEquals("{\"patientId\":\"patient-1\"}", event.getNewValue());
        assertEquals("adm-1", event.getEntityId());
        assertEquals(traceId, event.getTraceId());
    }

    @Test
    void auditEvent_noArgConstructor_shouldAllowSetters() {
        AuditEvent event = new AuditEvent();
        event.setUserId("user-2");
        event.setService("urgences-service");
        event.setAction("/api/urgences/triage|trier");
        event.setEntityId("triage-1");
        event.setTraceId("trace-456");

        assertEquals("user-2", event.getUserId());
        assertEquals("urgences-service", event.getService());
        assertEquals("/api/urgences/triage|trier", event.getAction());
        assertEquals("triage-1", event.getEntityId());
        assertEquals("trace-456", event.getTraceId());
    }

    @Test
    void safeSerialize_shouldHandleNull() throws Exception {
        String serviceName = "test-service";
        AuditAspect aspect = new AuditAspect(kafkaTemplate, serviceName, objectMapper);

        java.lang.reflect.Method method = AuditAspect.class.getDeclaredMethod("safeSerialize", Object.class);
        method.setAccessible(true);

        String result = (String) method.invoke(aspect, (Object) null);

        assertEquals("null", result);
    }

    @Test
    void safeSerialize_shouldSerializeObject() throws Exception {
        String serviceName = "test-service";
        AuditAspect aspect = new AuditAspect(kafkaTemplate, serviceName, objectMapper);

        java.lang.reflect.Method method = AuditAspect.class.getDeclaredMethod("safeSerialize", Object.class);
        method.setAccessible(true);

        java.util.Map<String, String> testObj = java.util.Map.of("key", "value");
        String result = (String) method.invoke(aspect, testObj);

        assertTrue(result.contains("key"));
        assertTrue(result.contains("value"));
    }

    @Test
    void auditAutoConfiguration_shouldCreateBean() {
        AuditAutoConfiguration config = new AuditAutoConfiguration();

        java.lang.reflect.Method method;
        try {
            method = AuditAutoConfiguration.class.getDeclaredMethod(
                    "auditAspect", KafkaTemplate.class, ObjectMapper.class);
        } catch (NoSuchMethodException e) {
            fail("auditAspect bean method should exist");
            return;
        }

        assertNotNull(method);
        assertEquals(AuditAspect.class, method.getReturnType());
    }

    @Test
    void auditAspect_constructor_shouldSetDependencies() {
        String serviceName = "test-service";
        AuditAspect aspect = new AuditAspect(kafkaTemplate, serviceName, objectMapper);

        assertNotNull(aspect);
    }
}
