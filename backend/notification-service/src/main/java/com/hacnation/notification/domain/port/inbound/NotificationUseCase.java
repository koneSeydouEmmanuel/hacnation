package com.hacnation.notification.domain.port.inbound;

import com.hacnation.common.dto.NotificationDto;
import java.util.List;

public interface NotificationUseCase {

    NotificationDto envoyer(String patientId, String canal, String contenu, String destinataire);

    List<NotificationDto> getHistorique(String patientId);

    void traiterNotificationEvent(String patientId, String canal, String contenu, String destinataire);
}
