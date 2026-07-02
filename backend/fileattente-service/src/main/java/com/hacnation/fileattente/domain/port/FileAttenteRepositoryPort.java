package com.hacnation.fileattente.domain.port;

import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.fileattente.domain.model.FileAttente;

import java.util.List;
import java.util.Optional;

public interface FileAttenteRepositoryPort {

    FileAttente save(FileAttente entry);

    List<FileAttente> findByServiceAndStatutOrderByPositionAsc(String service, StatutFileAttente statut);

    Optional<FileAttente> findByPatientIdAndStatut(String patientId, StatutFileAttente statut);

    Integer countByServiceAndStatut(String service, StatutFileAttente statut);
}
