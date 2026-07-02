package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.common.events.AuditEvent;
import com.hacnation.reportingadmin.domain.model.PisteAudit;
import com.hacnation.reportingadmin.domain.port.outbound.AuditRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AuditKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(AuditKafkaConsumer.class);

    private final AuditRepositoryPort auditRepository;

    public AuditKafkaConsumer(AuditRepositoryPort auditRepository) {
        this.auditRepository = auditRepository;
    }

    @KafkaListener(topics = "sic.audit", groupId = "audit-group")
    public void consumeAuditEvent(AuditEvent event) {
        log.debug("Audit recu: userId={}, action={}, service={}", event.getUserId(), event.getAction(), event.getService());

        PisteAudit pisteAudit = new PisteAudit();
        pisteAudit.setUserId(event.getUserId());
        pisteAudit.setTimestamp(event.getTimestamp());
        pisteAudit.setService(event.getService());
        pisteAudit.setAction(event.getAction());
        pisteAudit.setOldValue(event.getOldValue());
        pisteAudit.setNewValue(event.getNewValue());
        pisteAudit.setEntityId(event.getEntityId());

        auditRepository.save(pisteAudit);
    }
}
