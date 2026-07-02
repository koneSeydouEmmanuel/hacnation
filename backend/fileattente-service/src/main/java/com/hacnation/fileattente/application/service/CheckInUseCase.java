package com.hacnation.fileattente.application.service;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.common.exception.ResourceNotFoundException;
import com.hacnation.fileattente.domain.model.FileAttente;
import com.hacnation.fileattente.domain.port.FileAttenteRepositoryPort;
import com.hacnation.fileattente.domain.port.QueueEventPublisherPort;
import com.hacnation.fileattente.domain.port.RedisQueuePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CheckInUseCase {

    private static final Logger log = LoggerFactory.getLogger(CheckInUseCase.class);
    private static final int DUREE_CONSULTATION_DEFAUT = 30;

    private final FileAttenteRepositoryPort repository;
    private final RedisQueuePort redisQueue;
    private final QueueEventPublisherPort eventPublisher;

    public CheckInUseCase(FileAttenteRepositoryPort repository,
                          RedisQueuePort redisQueue,
                          QueueEventPublisherPort eventPublisher) {
        this.repository = repository;
        this.redisQueue = redisQueue;
        this.eventPublisher = eventPublisher;
    }

    public FileAttenteDto execute(String patientId, String rdvId, String service,
                                   String patientNom, String patientPrenom) {
        int waitingCount = repository.countByServiceAndStatut(service, StatutFileAttente.EN_ATTENTE);
        int position = waitingCount + 1;

        FileAttente entry = new FileAttente();
        entry.setPatientId(patientId);
        entry.setRdvId(rdvId);
        entry.setService(service);
        entry.setPatientNom(patientNom);
        entry.setPatientPrenom(patientPrenom);
        entry.setPosition(position);
        entry.setStatut(StatutFileAttente.EN_ATTENTE);
        entry.setTempsEstime(position * DUREE_CONSULTATION_DEFAUT);

        FileAttente saved = repository.save(entry);

        redisQueue.setPosition(service, patientId, position);

        eventPublisher.publishCheckIn(patientId, rdvId, service, position);

        log.info("Patient {} enregistre en file d'attente {}: position {}", patientId, service, position);
        return toDto(saved);
    }

    public List<FileAttenteDto> getFileAttente(String service) {
        return repository.findByServiceAndStatutOrderByPositionAsc(service, StatutFileAttente.EN_ATTENTE)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public FileAttenteDto checkinByQr(String qrData) {
        String[] parts = qrData.split(":");
        if (parts.length < 6 || !"RDV".equals(parts[0])) {
            throw new BusinessException("QR code invalide ou corrompu", 400);
        }
        String rdvId = parts[1];
        String patientId = parts[2];
        String nom = parts[3];
        String prenom = parts[4];
        String dateStr = parts[5];
        String service = parts.length > 6 ? parts[6] : "CONSULTATION";

        LocalDate rdvDate = LocalDate.parse(dateStr);
        if (rdvDate.isBefore(LocalDate.now())) {
            throw new BusinessException("Ce rendez-vous est expire", 400);
        }
        if (rdvDate.isAfter(LocalDate.now())) {
            throw new BusinessException("Ce rendez-vous n'est pas prevu pour aujourd'hui", 400);
        }

        var existing = repository.findByPatientIdAndStatut(patientId, StatutFileAttente.EN_ATTENTE);
        if (existing.isPresent()) {
            throw new BusinessException("Ce patient est deja enregistre", 409);
        }

        return execute(patientId, rdvId, service, nom, prenom);
    }

    public Map<String, Object> getPositionInfo(String patientId) {
        var entry = repository.findByPatientIdAndStatut(patientId, StatutFileAttente.EN_ATTENTE);
        if (entry.isEmpty()) {
            throw new ResourceNotFoundException("Patient non trouve dans la file d'attente", patientId);
        }
        FileAttente e = entry.get();
        int personnesDevant = e.getPosition() - 1;
        int tempsEstime = personnesDevant > 0 ? personnesDevant * DUREE_CONSULTATION_DEFAUT : 0;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("patientId", patientId);
        result.put("position", e.getPosition());
        result.put("personnesDevant", personnesDevant);
        result.put("tempsEstimeMinutes", tempsEstime);
        return result;
    }

    private FileAttenteDto toDto(FileAttente entry) {
        FileAttenteDto dto = new FileAttenteDto();
        dto.setId(entry.getId());
        dto.setPatientId(entry.getPatientId());
        dto.setRdvId(entry.getRdvId());
        dto.setPatientNom(entry.getPatientNom());
        dto.setPatientPrenom(entry.getPatientPrenom());
        dto.setService(entry.getService());
        dto.setPosition(entry.getPosition());
        dto.setStatut(entry.getStatut());
        dto.setHeureArrivee(entry.getHeureArrivee());
        dto.setTempsEstime(entry.getTempsEstime());
        return dto;
    }
}
