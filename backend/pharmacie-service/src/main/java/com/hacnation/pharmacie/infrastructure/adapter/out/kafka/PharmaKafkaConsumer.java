package com.hacnation.pharmacie.infrastructure.adapter.out.kafka;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.common.events.PrescriptionEvent;
import com.hacnation.pharmacie.domain.model.Ordonnance;
import com.hacnation.pharmacie.domain.port.OrdonnanceRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PharmaKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PharmaKafkaConsumer.class);

    private final OrdonnanceRepositoryPort ordonnanceRepository;

    public PharmaKafkaConsumer(OrdonnanceRepositoryPort ordonnanceRepository) {
        this.ordonnanceRepository = ordonnanceRepository;
    }

    @KafkaListener(topics = "sic.prescription", groupId = "pharmacie-group")
    public void consumePrescriptionEvent(PrescriptionEvent event) {
        String eventType = event.getEventType();
        String type = event.getType();

        if ("PRESCRIPTION_CREATED".equals(eventType)) {
            if (TypePrescription.MEDICAMENT.name().equals(type)) {
                Ordonnance ordonnance = new Ordonnance();
                ordonnance.setPrescriptionId(event.getPrescriptionId());
                ordonnance.setPatientId(event.getPatientId());
                ordonnance.setStatut(StatutPrescription.EN_ATTENTE);

                ordonnanceRepository.save(ordonnance);
                log.info("Ordonnance creee: prescriptionId={}, patientId={}",
                        event.getPrescriptionId(), event.getPatientId());
            } else {
                log.info("Type de prescription non gere par pharmacie: type={}, prescriptionId={}",
                        type, event.getPrescriptionId());
            }
        } else {
            log.info("PrescriptionEvent recu: type={}, eventType={}, prescriptionId={}",
                    type, eventType, event.getPrescriptionId());
        }
    }
}
