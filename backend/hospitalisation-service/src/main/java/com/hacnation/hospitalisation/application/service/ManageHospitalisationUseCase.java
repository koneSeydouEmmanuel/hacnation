package com.hacnation.hospitalisation.application.service;

import com.hacnation.common.enums.StatutLit;
import com.hacnation.common.events.AdmissionEvent;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.hospitalisation.domain.model.Hospitalisation;
import com.hacnation.hospitalisation.domain.model.Lit;
import com.hacnation.hospitalisation.domain.port.AdmissionEventPublisherPort;
import com.hacnation.hospitalisation.domain.port.HospitalisationRepositoryPort;
import com.hacnation.hospitalisation.domain.port.LitRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManageHospitalisationUseCase {

    private static final Logger log = LoggerFactory.getLogger(ManageHospitalisationUseCase.class);

    private final HospitalisationRepositoryPort hospitalisationRepository;
    private final LitRepositoryPort litRepository;
    private final AdmissionEventPublisherPort eventPublisher;

    public ManageHospitalisationUseCase(HospitalisationRepositoryPort hospitalisationRepository,
                                         LitRepositoryPort litRepository,
                                         AdmissionEventPublisherPort eventPublisher) {
        this.hospitalisationRepository = hospitalisationRepository;
        this.litRepository = litRepository;
        this.eventPublisher = eventPublisher;
    }

    public Hospitalisation admettre(String admissionId, String patientId, String service, String motif) {
        List<Lit> litsDisponibles = litRepository.findByServiceAndStatut(service, StatutLit.DISPONIBLE);

        if (litsDisponibles.isEmpty()) {
            throw new BusinessException("Aucun lit disponible dans le service: " + service, 409);
        }

        Lit lit = litsDisponibles.get(0);
        lit.setStatut(StatutLit.OCCUPE);
        lit.setPatientId(patientId);
        litRepository.save(lit);

        Hospitalisation hospitalisation = new Hospitalisation();
        hospitalisation.setAdmissionId(admissionId);
        hospitalisation.setPatientId(patientId);
        hospitalisation.setLitId(lit.getId());
        hospitalisation.setMotif(motif);
        hospitalisation.setDateEntree(LocalDateTime.now());
        hospitalisation.setStatut("EN_COURS");

        Hospitalisation saved = hospitalisationRepository.save(hospitalisation);

        log.info("Patient admis: hospitalisationId={}, patientId={}, litId={}", saved.getId(), patientId, lit.getId());
        return saved;
    }

    public Hospitalisation sortir(String hospitalisationId) {
        Hospitalisation hospitalisation = hospitalisationRepository.findById(hospitalisationId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospitalisation", hospitalisationId));

        hospitalisation.setDateSortie(LocalDateTime.now());
        hospitalisation.setStatut("TERMINE");
        Hospitalisation saved = hospitalisationRepository.save(hospitalisation);

        Lit lit = litRepository.findById(saved.getLitId())
                .orElseThrow(() -> new ResourceNotFoundException("Lit", saved.getLitId()));
        lit.setStatut(StatutLit.DISPONIBLE);
        lit.setPatientId(null);
        litRepository.save(lit);

        AdmissionEvent event = AdmissionEvent.created(saved.getId(), saved.getPatientId(), "SORTIE", "");
        event.setEventType("HOSPITALISATION_TERMINEE");
        eventPublisher.publishAdmissionEvent(event);

        log.info("Patient sorti: hospitalisationId={}, litId={}", hospitalisationId, lit.getId());
        return saved;
    }

    public List<Lit> getLits(String service) {
        return litRepository.findByService(service);
    }

    public long getLitsDisponibles(String service) {
        return litRepository.countByServiceAndStatut(service, StatutLit.DISPONIBLE);
    }
}
