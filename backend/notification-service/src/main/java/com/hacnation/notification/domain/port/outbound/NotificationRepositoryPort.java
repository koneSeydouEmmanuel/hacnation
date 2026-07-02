package com.hacnation.notification.domain.port.outbound;

import com.hacnation.notification.domain.model.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {

    Notification save(Notification notification);

    Optional<Notification> findById(String id);

    List<Notification> findByPatientIdOrderByDateEnvoiDesc(String patientId);
}
