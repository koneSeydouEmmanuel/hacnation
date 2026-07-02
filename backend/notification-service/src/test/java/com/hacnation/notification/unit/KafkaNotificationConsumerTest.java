package com.hacnation.notification.unit;

import com.hacnation.common.events.*;
import com.hacnation.notification.domain.port.inbound.NotificationUseCase;
import com.hacnation.notification.infrastructure.adapter.outbound.KafkaNotificationConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationConsumerTest {

    @Mock
    private NotificationUseCase notificationUseCase;

    @InjectMocks
    private KafkaNotificationConsumer kafkaConsumer;

    @Test
    void consumeNotificationEvent_shouldProcess() {
        NotificationEvent event = new NotificationEvent();
        event.setEventType("INVOICE_CREATED");
        event.setPatientId("patient-1");
        event.setCanal("SMS");
        event.setContenu("Test notification");
        event.setDestinataire("patient");

        kafkaConsumer.consumeNotificationEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), eq("Test notification"), eq("patient"));
    }

    @Test
    void consumePatientEvent_shouldProcessPatientCreated() {
        PatientEvent event = new PatientEvent();
        event.setEventType("PATIENT_CREATED");
        event.setPatientId("patient-1");
        event.setNom("Doe");
        event.setPrenom("John");

        kafkaConsumer.consumePatientEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("WHATSAPP"), anyString(), eq("accueil"));
    }

    @Test
    void consumeRdvEvent_shouldProcessConfirmed() {
        RdvEvent event = new RdvEvent();
        event.setEventType("RDV_CONFIRMED");
        event.setRdvId("rdv-1");
        event.setPatientId("patient-1");

        kafkaConsumer.consumeRdvEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), anyString(), eq("patient"));
    }

    @Test
    void consumeQueueEvent_shouldProcess() {
        QueueEvent event = new QueueEvent();
        event.setEventType("CHECK_IN");
        event.setPatientId("patient-1");
        event.setPosition(3);

        kafkaConsumer.consumeQueueEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("PUSH"), anyString(), eq("patient"));
    }

    @Test
    void consumeConsultationEvent_shouldProcessTerminated() {
        ConsultationEvent event = new ConsultationEvent();
        event.setEventType("CONSULTATION_TERMINATED");
        event.setConsultationId("consult-1");
        event.setPatientId("patient-1");

        kafkaConsumer.consumeConsultationEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), anyString(), eq("patient"));
    }

    @Test
    void consumePrescriptionEvent_shouldProcessExamen() {
        PrescriptionEvent event = new PrescriptionEvent();
        event.setEventType("PRESCRIPTION_CREATED");
        event.setPrescriptionId("presc-1");
        event.setType("EXAMEN");

        kafkaConsumer.consumePrescriptionEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("presc-1"), eq("PUSH"), anyString(), eq("laboratoire"));
    }

    @Test
    void consumeLabEvent_shouldProcessValidated() {
        LabEvent event = new LabEvent();
        event.setEventType("RESULT_VALIDATED");
        event.setAnalyseId("analyse-1");
        event.setPatientId("patient-1");

        kafkaConsumer.consumeLabEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), anyString(), eq("patient"));
    }

    @Test
    void consumePharmacyEvent_shouldProcessDispensed() {
        PharmacyEvent event = new PharmacyEvent();
        event.setEventType("DRUG_DISPENSED");
        event.setOrdonnanceId("ord-1");
        event.setPatientId("patient-1");

        kafkaConsumer.consumePharmacyEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), anyString(), eq("patient"));
    }

    @Test
    void consumeBillingEvent_shouldProcessInvoiceCreated() {
        BillingEvent event = new BillingEvent();
        event.setEventType("INVOICE_CREATED");
        event.setFactureId("facture-1");
        event.setPatientId("patient-1");
        event.setMontant(new BigDecimal("5000"));

        kafkaConsumer.consumeBillingEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("SMS"), anyString(), eq("patient"));
    }

    @Test
    void consumeAdmissionEvent_shouldProcess() {
        AdmissionEvent event = new AdmissionEvent();
        event.setEventType("ADMISSION_CREATED");
        event.setAdmissionId("adm-1");
        event.setPatientId("patient-1");
        event.setType("CONSULTATION");

        kafkaConsumer.consumeAdmissionEvent(event);

        verify(notificationUseCase).traiterNotificationEvent(eq("patient-1"), eq("EMAIL"), anyString(), eq("accueil"));
    }
}
