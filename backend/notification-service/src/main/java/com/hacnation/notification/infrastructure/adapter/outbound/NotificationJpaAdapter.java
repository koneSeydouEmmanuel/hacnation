package com.hacnation.notification.infrastructure.adapter.outbound;

import com.hacnation.notification.domain.model.Notification;
import com.hacnation.notification.domain.port.outbound.NotificationRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface NotificationJpaRepository extends JpaRepository<Notification, String> {

    List<Notification> findByPatientIdOrderByDateEnvoiDesc(String patientId);
}

@Repository
class NotificationJpaAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository jpaRepository;

    NotificationJpaAdapter(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return jpaRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Notification> findByPatientIdOrderByDateEnvoiDesc(String patientId) {
        return jpaRepository.findByPatientIdOrderByDateEnvoiDesc(patientId);
    }
}
