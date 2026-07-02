package com.hacnation.fileattente.application.service;

import com.hacnation.common.dto.FileAttenteDto;
import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.common.exception.BusinessException;
import com.hacnation.fileattente.domain.model.FileAttente;
import com.hacnation.fileattente.domain.port.FileAttenteRepositoryPort;
import com.hacnation.fileattente.domain.port.QueueEventPublisherPort;
import com.hacnation.fileattente.domain.port.RedisQueuePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CallNextUseCase {

    private static final Logger log = LoggerFactory.getLogger(CallNextUseCase.class);

    private final FileAttenteRepositoryPort repository;
    private final RedisQueuePort redisQueue;
    private final QueueEventPublisherPort eventPublisher;

    public CallNextUseCase(FileAttenteRepositoryPort repository,
                           RedisQueuePort redisQueue,
                           QueueEventPublisherPort eventPublisher) {
        this.repository = repository;
        this.redisQueue = redisQueue;
        this.eventPublisher = eventPublisher;
    }

    public FileAttenteDto execute(String service) {
        List<FileAttente> waiting = repository
                .findByServiceAndStatutOrderByPositionAsc(service, StatutFileAttente.EN_ATTENTE);

        if (waiting.isEmpty()) {
            throw new BusinessException("Aucun patient en attente pour le service: " + service, 404);
        }

        FileAttente next = waiting.get(0);
        next.setStatut(StatutFileAttente.APPELE);
        FileAttente updated = repository.save(next);

        redisQueue.removePosition(service, updated.getPatientId());

        for (int i = 1; i < waiting.size(); i++) {
            FileAttente entry = waiting.get(i);
            int newPosition = i;
            entry.setPosition(newPosition);
            entry.setTempsEstime(newPosition * 15);
            repository.save(entry);

            redisQueue.setPosition(service, entry.getPatientId(), newPosition);
        }

        eventPublisher.publishCallNext(updated.getPatientId(), service);

        log.info("Patient {} appele pour service {}", updated.getPatientId(), service);
        return toDto(updated);
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
