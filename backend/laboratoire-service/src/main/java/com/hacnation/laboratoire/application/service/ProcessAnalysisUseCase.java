package com.hacnation.laboratoire.application.service;

import com.hacnation.common.enums.StatutPrescription;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.laboratoire.domain.model.DemandeAnalyse;
import com.hacnation.laboratoire.domain.port.DemandeAnalyseRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessAnalysisUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessAnalysisUseCase.class);

    private final DemandeAnalyseRepositoryPort demandeAnalyseRepository;

    public ProcessAnalysisUseCase(DemandeAnalyseRepositoryPort demandeAnalyseRepository) {
        this.demandeAnalyseRepository = demandeAnalyseRepository;
    }

    public Page<DemandeAnalyse> getDemandesEnAttente(Pageable pageable) {
        return demandeAnalyseRepository.findByStatutOrderByCreatedAtAsc(StatutPrescription.EN_ATTENTE, pageable);
    }

    @Transactional
    public DemandeAnalyse saisirResultats(String analyseId, String resultatsJson, String laborantinId) {
        DemandeAnalyse analyse = demandeAnalyseRepository.findById(analyseId)
                .orElseThrow(() -> new ResourceNotFoundException("DemandeAnalyse", analyseId));

        analyse.setResultats(resultatsJson);
        analyse.setStatut(StatutPrescription.EN_COURS);
        analyse.setLaborantinId(laborantinId);
        analyse.setDateResultat(LocalDateTime.now());

        DemandeAnalyse saved = demandeAnalyseRepository.save(analyse);
        log.info("Resultats saisis pour analyse: id={}, laborantin={}", analyseId, laborantinId);
        return saved;
    }

    public List<DemandeAnalyse> getDemandesByPatient(String patientId) {
        return demandeAnalyseRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    public DemandeAnalyse getDemandeById(String id) {
        return demandeAnalyseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DemandeAnalyse", id));
    }
}
