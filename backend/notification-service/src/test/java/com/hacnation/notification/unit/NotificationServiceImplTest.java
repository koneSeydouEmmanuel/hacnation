package com.hacnation.notification.unit;

import com.hacnation.common.dto.NotificationDto;
import com.hacnation.notification.application.service.NotificationServiceImpl;
import com.hacnation.notification.domain.model.Notification;
import com.hacnation.notification.domain.port.outbound.NotificationRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepositoryPort notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void envoyer_sms_shouldSendAndSave() {
        Notification saved = new Notification();
        saved.setId("notif-1");
        saved.setPatientId("patient-1");
        saved.setCanal("SMS");
        saved.setContenu("Votre rendez-vous est confirme");
        saved.setStatut("ENVOYE");

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        NotificationDto result = notificationService.envoyer("patient-1", "SMS",
                "Votre rendez-vous est confirme", "patient");

        assertNotNull(result);
        assertEquals("SMS", result.getCanal().name());
        assertEquals("ENVOYE", result.getStatut());
    }

    @Test
    void envoyer_whatsapp_shouldSendAndSave() {
        Notification saved = new Notification();
        saved.setId("notif-2");
        saved.setPatientId("patient-1");
        saved.setCanal("WHATSAPP");
        saved.setContenu("Notification WhatsApp");
        saved.setStatut("ENVOYE");

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        NotificationDto result = notificationService.envoyer("patient-1", "WHATSAPP",
                "Notification WhatsApp", "patient");

        assertNotNull(result);
        assertEquals("WHATSAPP", result.getCanal().name());
    }

    @Test
    void envoyer_email_shouldSendAndSave() {
        Notification saved = new Notification();
        saved.setId("notif-3");
        saved.setPatientId("patient-1");
        saved.setCanal("EMAIL");
        saved.setContenu("Notification email");
        saved.setStatut("ENVOYE");

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        NotificationDto result = notificationService.envoyer("patient-1", "EMAIL",
                "Notification email", "patient");

        assertNotNull(result);
        assertEquals("EMAIL", result.getCanal().name());
    }

    @Test
    void getHistorique_shouldReturnList() {
        when(notificationRepository.findByPatientIdOrderByDateEnvoiDesc("patient-1"))
                .thenReturn(Collections.emptyList());

        List<NotificationDto> historique = notificationService.getHistorique("patient-1");

        assertNotNull(historique);
        assertTrue(historique.isEmpty());
    }

    @Test
    void traiterNotificationEvent_shouldCallEnvoyer() {
        Notification saved = new Notification();
        saved.setId("notif-1");
        saved.setPatientId("patient-1");
        saved.setCanal("SMS");
        saved.setContenu("Test");
        saved.setStatut("ENVOYE");

        when(notificationRepository.save(any(Notification.class))).thenReturn(saved);

        notificationService.traiterNotificationEvent("patient-1", "SMS", "Test", "patient");

        verify(notificationRepository, atLeastOnce()).save(any(Notification.class));
    }
}
