package com.hacnation.prescription.infrastructure.adapter.out.kafka;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.events.LabEvent;
import com.hacnation.prescription.domain.model.Prescription;
import com.hacnation.prescription.domain.port.PrescriptionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PrescriptionKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PrescriptionKafkaConsumer.class);

    private final PrescriptionRepositoryPort prescriptionRepository;

    public PrescriptionKafkaConsumer(PrescriptionRepositoryPort prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @KafkaListener(topics = "sic.laboratoire", groupId = "prescription-group")
    @Transactional
    public void consumeLabEvent(LabEvent event) {
        String eventType = event.getEventType();
        if ("LAB_RESULT_AVAILABLE".equals(eventType)) {
            String prescriptionId = event.getPrescriptionId();
            if (prescriptionId != null) {
                prescriptionRepository.findById(prescriptionId).ifPresent(prescription -> {
                    prescription.setStatut(StatutPrescription.TERMINEE);
                    prescriptionRepository.save(prescription);
                    log.info("Prescription terminee suite resultat labo: prescriptionId={}", prescriptionId);
                });
            }
        }
    }
}
