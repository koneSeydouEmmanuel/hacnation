package com.hacnation.reportingadmin.infrastructure.adapter.outbound;

import com.hacnation.reportingadmin.domain.model.PisteAudit;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AuditJpaRepository extends JpaRepository<PisteAudit, String> {
    List<PisteAudit> findByAction(String action);
    List<PisteAudit> findByUserId(String userId);
    List<PisteAudit> findByService(String service);
    List<PisteAudit> findByTimestampBetween(LocalDateTime debut, LocalDateTime fin);
}

@Repository
class AuditJpaAdapter implements com.hacnation.reportingadmin.domain.port.outbound.AuditRepositoryPort {

    private final AuditJpaRepository jpaRepository;

    AuditJpaAdapter(AuditJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PisteAudit save(PisteAudit audit) {
        return jpaRepository.save(audit);
    }

    @Override
    public java.util.Optional<PisteAudit> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<PisteAudit> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<PisteAudit> findByAction(String action) {
        return jpaRepository.findByAction(action);
    }

    @Override
    public List<PisteAudit> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public List<PisteAudit> findByService(String service) {
        return jpaRepository.findByService(service);
    }

    @Override
    public List<PisteAudit> findByTimestampBetween(LocalDateTime debut, LocalDateTime fin) {
        return jpaRepository.findByTimestampBetween(debut, fin);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
