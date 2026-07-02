package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.PisteAudit;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuditRepositoryPort {

    PisteAudit save(PisteAudit audit);
    Optional<PisteAudit> findById(String id);
    List<PisteAudit> findAll();
    List<PisteAudit> findByAction(String action);
    List<PisteAudit> findByUserId(String userId);
    List<PisteAudit> findByService(String service);
    List<PisteAudit> findByTimestampBetween(LocalDateTime debut, LocalDateTime fin);
    void deleteById(String id);
}
