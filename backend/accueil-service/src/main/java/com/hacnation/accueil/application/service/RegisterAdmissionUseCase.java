package com.hacnation.accueil.application.service;

import com.hacnation.common.enums.TypeAdmission;
import com.hacnation.common.events.AdmissionEvent;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.accueil.domain.model.Admission;
import com.hacnation.accueil.domain.port.AdmissionEventPublisherPort;
import com.hacnation.accueil.domain.port.AdmissionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class RegisterAdmissionUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterAdmissionUseCase.class);

    private final AdmissionRepositoryPort admissionRepository;
    private final AdmissionEventPublisherPort eventPublisher;

    public RegisterAdmissionUseCase(AdmissionRepositoryPort admissionRepository,
                                     AdmissionEventPublisherPort eventPublisher) {
        this.admissionRepository = admissionRepository;
        this.eventPublisher = eventPublisher;
    }

    public Admission createAdmission(String patientId, TypeAdmission type, String service, String motif) {
        Admission admission = new Admission();
        admission.setPatientId(patientId);
        admission.setType(type);
        admission.setService(service);
        admission.setMotif(motif);
        admission.setStatut("ENREGISTREE");
        admission.setDateAdmission(LocalDateTime.now());

        Admission saved = admissionRepository.save(admission);

        AdmissionEvent event = AdmissionEvent.created(
                saved.getId(), saved.getPatientId(), saved.getType().name(), saved.getService());
        eventPublisher.publishAdmissionEvent(event);

        log.info("Admission creee: id={}, patientId={}, type={}", saved.getId(), patientId, type);
        return saved;
    }

    public Admission getAdmission(String id) {
        return admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission", id));
    }

    public List<Admission> getAdmissionsByPatient(String patientId) {
        return admissionRepository.findByPatientId(patientId);
    }
}
