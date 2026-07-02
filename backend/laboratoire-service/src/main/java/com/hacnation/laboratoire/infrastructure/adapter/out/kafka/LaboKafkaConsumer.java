package com.hacnation.laboratoire.infrastructure.adapter.out.kafka;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.enums.TypePrescription;
import com.hacnation.common.events.PrescriptionEvent;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class LaboKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(LaboKafkaConsumer.class);

    private final DemandeAnalyseRepositoryPort demandeAnalyseRepository;

    public LaboKafkaConsumer(DemandeAnalyseRepositoryPort demandeAnalyseRepository) {
        this.demandeAnalyseRepository = demandeAnalyseRepository;
    }

    @KafkaListener(topics = "sic.prescription", groupId = "laboratoire-group")
    public void consumePrescriptionEvent(PrescriptionEvent event) {
        String eventType = event.getEventType();
        String type = event.getType();

        if ("PRESCRIPTION_CREATED".equals(eventType)) {
            if (TypePrescription.EXAMEN.name().equals(type)) {
                DemandeAnalyse demande = new DemandeAnalyse();
                demande.setPrescriptionId(event.getPrescriptionId());
                demande.setPatientId(event.getPatientId());
                demande.setStatut(StatutPrescription.EN_ATTENTE);

                demandeAnalyseRepository.save(demande);
                log.info("DemandeAnalyse creee: prescriptionId={}, patientId={}",
                        event.getPrescriptionId(), event.getPatientId());
            } else {
                log.info("Type de prescription non gere par laboratoire: type={}, prescriptionId={}",
                        type, event.getPrescriptionId());
            }
        } else {
            log.info("PrescriptionEvent recu: type={}, eventType={}, prescriptionId={}",
                    type, eventType, event.getPrescriptionId());
        }
    }
}
