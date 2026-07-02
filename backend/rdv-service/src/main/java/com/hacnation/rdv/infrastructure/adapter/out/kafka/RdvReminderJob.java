package com.hacnation.rdv.infrastructure.adapter.out.kafka;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import com.hacnation.rdv.domain.model.RendezVous;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class RdvReminderJob {

    private static final Logger log = LoggerFactory.getLogger(RdvReminderJob.class);
    private static final String NOTIFICATION_EVENTS_TOPIC = "notification-events";

    private final RdvRepositoryPort rdvRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RdvReminderJob(RdvRepositoryPort rdvRepository,
                          KafkaTemplate<String, Object> kafkaTemplate) {
        this.rdvRepository = rdvRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void sendRdVReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in24Hours = now.plusHours(24);

        List<RendezVous> upcomingRdvs = rdvRepository
                .findByStatutAndDateHeureBetween(StatutRdv.CONFIRME, now, in24Hours);

        log.info("Envoi de {} rappels de rendez-vous", upcomingRdvs.size());

        for (RendezVous rdv : upcomingRdvs) {
            NotificationEventPayload event = new NotificationEventPayload();
            event.setEventId(UUID.randomUUID().toString());
            event.setEventType("RDV_REMINDER");
            event.setPatientId(rdv.getPatientId());
            event.setCanal("SMS");
            event.setContenu("Rappel: Vous avez un rendez-vous le "
                    + rdv.getDateHeure().toLocalDate()
                    + " a " + rdv.getDateHeure().toLocalTime()
                    + " pour le service " + rdv.getService() + ".");
            event.setDestinataire(rdv.getPatientId());

            kafkaTemplate.send(NOTIFICATION_EVENTS_TOPIC, rdv.getPatientId(), event);
            log.info("Rappel envoye pour RDV {}", rdv.getId());
        }
    }

    public static class NotificationEventPayload {
        private String eventId;
        private String eventType;
        private String patientId;
        private String canal;
        private String contenu;
        private String destinataire;
        private LocalDateTime timestamp;

        public NotificationEventPayload() {
            this.timestamp = LocalDateTime.now();
        }

        public String getEventId() { return eventId; }
        public void setEventId(String eventId) { this.eventId = eventId; }
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }
        public String getPatientId() { return patientId; }
        public void setPatientId(String patientId) { this.patientId = patientId; }
        public String getCanal() { return canal; }
        public void setCanal(String canal) { this.canal = canal; }
        public String getContenu() { return contenu; }
        public void setContenu(String contenu) { this.contenu = contenu; }
        public String getDestinataire() { return destinataire; }
        public void setDestinataire(String destinataire) { this.destinataire = destinataire; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }
}
