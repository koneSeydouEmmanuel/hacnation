package com.hacnation.notification.application.service;

import com.hacnation.common.dto.NotificationDto;
import com.hacnation.common.enums.CanalNotification;
import com.hacnation.notification.domain.model.Notification;
import com.hacnation.notification.domain.port.inbound.NotificationUseCase;
import com.hacnation.notification.domain.port.outbound.NotificationRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepositoryPort notificationRepository;

    public NotificationServiceImpl(NotificationRepositoryPort notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    @Transactional
    public NotificationDto envoyer(String patientId, String canal, String contenu, String destinataire) {
        log.info("=== ENVOI NOTIFICATION ===");
        log.info("Patient: {} | Canal: {} | Destinataire: {}", patientId, canal, destinataire);
        log.info("Contenu: {}", contenu);

        simulerEnvoi(canal, destinataire, contenu);

        Notification notification = new Notification();
        notification.setPatientId(patientId);
        notification.setCanal(canal);
        notification.setContenu(contenu);
        notification.setDestinataire(destinataire);
        notification.setStatut("ENVOYE");
        notification.setDateEnvoi(LocalDateTime.now());

        notification = notificationRepository.save(notification);
        log.info("Notification sauvegardee avec id={}", notification.getId());

        return toDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getHistorique(String patientId) {
        return notificationRepository.findByPatientIdOrderByDateEnvoiDesc(patientId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void traiterNotificationEvent(String patientId, String canal, String contenu, String destinataire) {
        log.info("Traitement notification event depuis Kafka - Patient: {}, Canal: {}", patientId, canal);
        try {
            envoyer(patientId, canal, contenu, destinataire);
        } catch (Exception e) {
            log.warn("Echec canal primaire {} pour patient {}, tentative fallback SMS", canal, patientId);
            try {
                envoyer(patientId, "SMS", contenu, destinataire);
            } catch (Exception e2) {
                log.error("Echec total notification: patientId={}, canaux tentes={}, raison={}",
                        patientId, canal + ",SMS", e2.getMessage());
            }
        }
    }

    private void simulerEnvoi(String canal, String destinataire, String contenu) {
        switch (canal.toUpperCase()) {
            case "WHATSAPP":
                log.info("Simulating [WHATSAPP] to [{}]: [{}]", destinataire, contenu);
                break;
            case "SMS":
                log.info("Simulating [SMS] to [{}]: [{}]", destinataire, contenu);
                break;
            case "EMAIL":
                log.info("Simulating [EMAIL] to [{}]: [{}]", destinataire, contenu);
                break;
            case "PUSH":
                log.info("Simulating [PUSH] to [{}]: [{}]", destinataire, contenu);
                break;
            default:
                log.info("Simulating [{}] to [{}]: [{}]", canal.toUpperCase(), destinataire, contenu);
        }
    }

    private NotificationDto toDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setPatientId(notification.getPatientId());
        dto.setCanal(CanalNotification.valueOf(notification.getCanal()));
        dto.setContenu(notification.getContenu());
        dto.setStatut(notification.getStatut());
        dto.setDateEnvoi(notification.getDateEnvoi());
        return dto;
    }
}
