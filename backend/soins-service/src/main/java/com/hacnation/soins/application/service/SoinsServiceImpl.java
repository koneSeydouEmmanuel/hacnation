package com.hacnation.soins.application.service;

import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.soins.domain.model.SoinInfirmier;
import com.hacnation.soins.domain.port.inbound.SoinsUseCase;
import com.hacnation.soins.domain.port.outbound.SoinInfirmierRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SoinsServiceImpl implements SoinsUseCase {

    private static final Logger log = LoggerFactory.getLogger(SoinsServiceImpl.class);

    private final SoinInfirmierRepositoryPort soinInfirmierRepository;

    public SoinsServiceImpl(SoinInfirmierRepositoryPort soinInfirmierRepository) {
        this.soinInfirmierRepository = soinInfirmierRepository;
    }

    @Override
    @Transactional
    public SoinInfirmier creerPlanSoins(String hospitalisationId, String patientId,
                                        String typeSoin, String description,
                                        String frequence, String instructions) {
        SoinInfirmier soin = new SoinInfirmier();
        soin.setHospitalisationId(hospitalisationId);
        soin.setPatientId(patientId);
        soin.setTypeSoin(typeSoin);
        soin.setDescription(description);
        soin.setFrequence(frequence);
        soin.setInstructions(instructions);
        soin.setStatut("EN_ATTENTE");

        SoinInfirmier saved = soinInfirmierRepository.save(soin);

        log.info("Plan de soins cree: id={}, patientId={}, type={}", saved.getId(), patientId, typeSoin);
        return saved;
    }

    @Override
    @Transactional
    public SoinInfirmier administrerSoin(String soinId, String infirmierId) {
        SoinInfirmier soin = soinInfirmierRepository.findById(soinId)
                .orElseThrow(() -> new ResourceNotFoundException("SoinInfirmier", soinId));

        soin.setStatut("ADMINISTRE");
        soin.setInfirmierId(infirmierId);
        soin.setDateAdministration(LocalDateTime.now());

        SoinInfirmier saved = soinInfirmierRepository.save(soin);

        log.info("Soin administre: id={}, infirmierId={}", soinId, infirmierId);
        return saved;
    }

    @Override
    public List<SoinInfirmier> getSoinsPatient(String patientId) {
        return soinInfirmierRepository.findByPatientIdAndStatut(patientId, "EN_ATTENTE");
    }

    @Override
    @Transactional
    public SoinInfirmier nonAdministre(String soinId, String motif) {
        SoinInfirmier soin = soinInfirmierRepository.findById(soinId)
                .orElseThrow(() -> new ResourceNotFoundException("SoinInfirmier", soinId));

        soin.setStatut("NON_ADMINISTRE");
        soin.setMotifNonAdministration(motif);

        SoinInfirmier saved = soinInfirmierRepository.save(soin);

        log.info("Soin non administre: id={}, motif={}", soinId, motif);
        return saved;
    }
}
