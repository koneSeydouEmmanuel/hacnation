package com.hacnation.rdv.application.service;

import com.hacnation.common.dto.RdvDto;
import com.hacnation.common.enums.StatutRdv;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvEventPublisherPort;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreateRdvUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateRdvUseCase.class);

    private final RdvRepositoryPort rdvRepository;
    private final QrCodeService qrCodeService;
    private final RdvEventPublisherPort eventPublisher;

    public CreateRdvUseCase(RdvRepositoryPort rdvRepository,
                            QrCodeService qrCodeService,
                            RdvEventPublisherPort eventPublisher) {
        this.rdvRepository = rdvRepository;
        this.qrCodeService = qrCodeService;
        this.eventPublisher = eventPublisher;
    }

    public RdvDto createRdv(String patientId, String praticienId, String service,
                            LocalDateTime dateHeure, String motif) {
        if (praticienId != null) {
            LocalDateTime slotStart = dateHeure.withMinute(0).withSecond(0).withNano(0);
            LocalDateTime slotEnd = slotStart.plusHours(1);
            List<RendezVous> conflicts = rdvRepository.findByPraticienIdAndDateHeureBetween(praticienId, slotStart, slotEnd);
            conflicts = conflicts.stream()
                    .filter(r -> r.getStatut() != StatutRdv.ANNULE)
                    .collect(Collectors.toList());
            if (!conflicts.isEmpty()) {
                throw new BusinessException("Creneau deja reserve pour ce praticien", 409);
            }
        }

        RendezVous rdv = new RendezVous();
        rdv.setPatientId(patientId);
        rdv.setPraticienId(praticienId);
        rdv.setService(service);
        rdv.setDateHeure(dateHeure);
        rdv.setMotif(motif);
        rdv.setStatut(StatutRdv.EN_ATTENTE);

        RendezVous saved = rdvRepository.save(rdv);

        saved.setQrCode(qrCodeService.generateQrCode(saved.getId(), saved.getPatientId()));
        saved = rdvRepository.save(saved);

        eventPublisher.publishRdvCreated(saved.getId(), saved.getPatientId(),
                saved.getService(), saved.getDateHeure());

        log.info("Rendez-vous cree: {}", saved.getId());
        return toDto(saved);
    }

    public RdvDto getRdv(String id) {
        RendezVous rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous", id));
        return toDto(rdv);
    }

    public List<RdvDto> getRdvsByPatient(String patientId) {
        return rdvRepository.findByPatientIdOrderByDateHeureDesc(patientId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public RdvDto updateStatut(String id, String newStatut) {
        RendezVous rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous", id));
        try {
            StatutRdv statut = StatutRdv.valueOf(newStatut.toUpperCase());
            rdv.setStatut(statut);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Statut invalide: " + newStatut);
        }
        RendezVous updated = rdvRepository.save(rdv);

        eventPublisher.publishRdvStatusChanged(updated.getId(), updated.getPatientId(), newStatut);

        log.info("Statut RDV mis a jour: {} -> {}", id, newStatut);
        return toDto(updated);
    }

    public RdvDto cancelRdv(String id) {
        RendezVous rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous", id));
        rdv.setStatut(StatutRdv.ANNULE);
        RendezVous updated = rdvRepository.save(rdv);

        eventPublisher.publishRdvStatusChanged(updated.getId(), updated.getPatientId(),
                StatutRdv.ANNULE.name());

        log.info("Rendez-vous annule: {}", id);
        return toDto(updated);
    }

    public RdvDto honorRdv(String id) {
        RendezVous rdv = rdvRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RendezVous", id));
        rdv.setStatut(StatutRdv.HONORE);
        RendezVous updated = rdvRepository.save(rdv);

        eventPublisher.publishRdvStatusChanged(updated.getId(), updated.getPatientId(),
                StatutRdv.HONORE.name());

        log.info("Rendez-vous honore: {}", id);
        return toDto(updated);
    }

    private RdvDto toDto(RendezVous rdv) {
        RdvDto dto = new RdvDto();
        dto.setId(rdv.getId());
        dto.setPatientId(rdv.getPatientId());
        dto.setPraticienId(rdv.getPraticienId());
        dto.setService(rdv.getService());
        dto.setDateHeure(rdv.getDateHeure());
        dto.setStatut(rdv.getStatut());
        dto.setQrCode(rdv.getQrCode());
        dto.setMotif(rdv.getMotif());
        dto.setCreatedAt(rdv.getCreatedAt());
        dto.setUpdatedAt(rdv.getUpdatedAt());
        return dto;
    }
}
