package com.hacnation.notification.infrastructure.adapter.outbound;

import com.hacnation.common.events.AdmissionEvent;
import com.hacnation.common.events.BillingEvent;
import com.hacnation.common.events.ConsultationEvent;
import com.hacnation.common.events.LabEvent;
import com.hacnation.common.events.NotificationEvent;
import com.hacnation.common.events.PatientEvent;
import com.hacnation.common.events.PharmacyEvent;
import com.hacnation.common.events.PrescriptionEvent;
import com.hacnation.common.events.QueueEvent;
import com.hacnation.common.events.RdvEvent;
import com.hacnation.notification.domain.port.inbound.NotificationUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaNotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaNotificationConsumer.class);

    private final NotificationUseCase notificationUseCase;

    public KafkaNotificationConsumer(NotificationUseCase notificationUseCase) {
        this.notificationUseCase = notificationUseCase;
    }

    @KafkaListener(topics = "sic.notification", groupId = "notification-group")
    public void consumeNotificationEvent(NotificationEvent event) {
        log.info("=== NotificationEvent recu: type={}, patientId={}, canal={}, traceId={} ===",
                event.getEventType(), event.getPatientId(), event.getCanal(), event.getTraceId());
        notificationUseCase.traiterNotificationEvent(
                event.getPatientId(), event.getCanal(), event.getContenu(), event.getDestinataire());
    }

    @KafkaListener(topics = "sic.patient", groupId = "notification-group")
    public void consumePatientEvent(PatientEvent event) {
        log.info("=== PatientEvent recu: type={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getPatientId(), event.getTraceId());
        if ("PATIENT_CREATED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "WHATSAPP",
                    "Nouveau patient enregistre: " + event.getNom() + " " + event.getPrenom(),
                    "accueil");
        }
    }

    @KafkaListener(topics = "sic.rdv", groupId = "notification-group")
    public void consumeRdvEvent(RdvEvent event) {
        log.info("=== RdvEvent recu: type={}, rdvId={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getRdvId(), event.getPatientId(), event.getTraceId());
        if ("RDV_CONFIRMED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Votre rendez-vous est confirme. QR code disponible.",
                    "patient");
        }
    }

    @KafkaListener(topics = "sic.file-attente", groupId = "notification-group")
    public void consumeQueueEvent(QueueEvent event) {
        log.info("=== QueueEvent recu: type={}, patientId={}, position={}, traceId={} ===",
                event.getEventType(), event.getPatientId(), event.getPosition(), event.getTraceId());
        notificationUseCase.traiterNotificationEvent(
                event.getPatientId(), "PUSH",
                "Votre position dans la file d'attente: " + event.getPosition(),
                "patient");
    }

    @KafkaListener(topics = "sic.consultation", groupId = "notification-group")
    public void consumeConsultationEvent(ConsultationEvent event) {
        log.info("=== ConsultationEvent recu: type={}, consultationId={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getConsultationId(), event.getPatientId(), event.getTraceId());
        if ("CONSULTATION_TERMINATED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Votre consultation est terminee",
                    "patient");
        }
    }

    @KafkaListener(topics = "sic.prescription", groupId = "notification-group")
    public void consumePrescriptionEvent(PrescriptionEvent event) {
        log.info("=== PrescriptionEvent recu: type={}, prescriptionId={}, eventType={}, traceId={} ===",
                event.getEventType(), event.getPrescriptionId(), event.getType(), event.getTraceId());
        if ("EXAMEN".equals(event.getType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPrescriptionId(), "PUSH",
                    "Nouvelle demande d'examen de type: " + event.getType(),
                    "laboratoire");
        }
    }

    @KafkaListener(topics = "sic.laboratoire", groupId = "notification-group")
    public void consumeLabEvent(LabEvent event) {
        log.info("=== LabEvent recu: type={}, analyseId={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getAnalyseId(), event.getPatientId(), event.getTraceId());
        if ("RESULT_VALIDATED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Vos resultats d'examen sont disponibles",
                    "patient");
        }
    }

    @KafkaListener(topics = "sic.pharmacie", groupId = "notification-group")
    public void consumePharmacyEvent(PharmacyEvent event) {
        log.info("=== PharmacyEvent recu: type={}, patientId={}, ordonnanceId={}, traceId={} ===",
                event.getEventType(), event.getPatientId(), event.getOrdonnanceId(), event.getTraceId());
        if ("DRUG_DISPENSED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Vos medicaments sont prets a la pharmacie",
                    "patient");
        }
    }

    @KafkaListener(topics = "sic.facturation", groupId = "notification-group")
    public void consumeBillingEvent(BillingEvent event) {
        log.info("=== BillingEvent recu: type={}, factureId={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getFactureId(), event.getPatientId(), event.getTraceId());
        if ("INVOICE_CREATED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Votre facture de " + event.getMontant() + " FCFA est disponible",
                    "patient");
        }
        if ("PAYMENT_RECEIVED".equals(event.getEventType())) {
            notificationUseCase.traiterNotificationEvent(
                    event.getPatientId(), "SMS",
                    "Paiement de " + event.getMontant() + " FCFA confirme. Merci.",
                    "patient");
        }
    }

    @KafkaListener(topics = "sic.admission", groupId = "notification-group")
    public void consumeAdmissionEvent(AdmissionEvent event) {
        log.info("=== AdmissionEvent recu: type={}, admissionId={}, patientId={}, traceId={} ===",
                event.getEventType(), event.getAdmissionId(), event.getPatientId(), event.getTraceId());
        notificationUseCase.traiterNotificationEvent(
                event.getPatientId(), "EMAIL",
                "Admission enregistree: " + event.getType(),
                "accueil");
    }
}
