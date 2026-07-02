package com.hacnation.consultation.infrastructure.adapter.out.kafka;

import com.hacnation.common.events.RdvEvent;
import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConsultationKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ConsultationKafkaConsumer.class);

    private final ConsultationRepositoryPort consultationRepository;

    public ConsultationKafkaConsumer(ConsultationRepositoryPort consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @KafkaListener(topics = "sic.rdv", groupId = "consultation-group")
    @Transactional
    public void consumeRdvEvent(RdvEvent event) {
        String eventType = event.getEventType();
        if ("RDV_STATUS_CHANGED".equals(eventType) && "HONORE".equals(event.getStatut())) {
            String rdvId = event.getRdvId();
            Optional<Consultation> optConsultation = consultationRepository.findAll()
                    .stream()
                    .filter(c -> rdvId.equals(c.getRdvId()))
                    .findFirst();
            if (optConsultation.isPresent()) {
                Consultation consultation = optConsultation.get();
                log.info("RDV honore: rdvId={}, consultationId={}", rdvId, consultation.getId());
            } else {
                log.info("RDV honore mais aucune consultation liee: rdvId={}", rdvId);
            }
        }
    }
}
