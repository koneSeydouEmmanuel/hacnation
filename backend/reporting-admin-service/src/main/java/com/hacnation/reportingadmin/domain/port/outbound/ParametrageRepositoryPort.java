package com.hacnation.reportingadmin.domain.port.outbound;

import com.hacnation.reportingadmin.domain.model.ParametrageSysteme;
import java.util.List;
import java.util.Optional;

public interface ParametrageRepositoryPort {

    ParametrageSysteme save(ParametrageSysteme parametrage);
    Optional<ParametrageSysteme> findById(String id);
    Optional<ParametrageSysteme> findByCle(String cle);
    List<ParametrageSysteme> findAll();
    void deleteById(String id);
}
