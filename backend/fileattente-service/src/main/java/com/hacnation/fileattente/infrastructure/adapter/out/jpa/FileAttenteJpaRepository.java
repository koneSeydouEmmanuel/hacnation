package com.hacnation.fileattente.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutFileAttente;
import com.hacnation.fileattente.domain.model.FileAttente;
import com.hacnation.fileattente.domain.port.FileAttenteRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FileAttenteJpaRepository implements FileAttenteRepositoryPort {

    private final SpringFileAttenteJpaRepository jpaRepository;

    public FileAttenteJpaRepository(SpringFileAttenteJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FileAttente save(FileAttente entry) {
        FileAttenteEntity entity = toEntity(entry);
        FileAttenteEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<FileAttente> findByServiceAndStatutOrderByPositionAsc(String service, StatutFileAttente statut) {
        return jpaRepository.findByServiceAndStatutOrderByPositionAsc(service, statut)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<FileAttente> findByPatientIdAndStatut(String patientId, StatutFileAttente statut) {
        return jpaRepository.findByPatientIdAndStatut(patientId, statut).map(this::toDomain);
    }

    @Override
    public Integer countByServiceAndStatut(String service, StatutFileAttente statut) {
        return jpaRepository.countByServiceAndStatut(service, statut);
    }

    private FileAttenteEntity toEntity(FileAttente entry) {
        FileAttenteEntity entity = new FileAttenteEntity();
        entity.setId(entry.getId());
        entity.setPatientId(entry.getPatientId());
        entity.setRdvId(entry.getRdvId());
        entity.setPatientNom(entry.getPatientNom());
        entity.setPatientPrenom(entry.getPatientPrenom());
        entity.setService(entry.getService());
        entity.setPosition(entry.getPosition());
        entity.setStatut(entry.getStatut());
        entity.setHeureArrivee(entry.getHeureArrivee());
        entity.setTempsEstime(entry.getTempsEstime());
        entity.setCreatedAt(entry.getCreatedAt());
        return entity;
    }

    private FileAttente toDomain(FileAttenteEntity entity) {
        FileAttente entry = new FileAttente();
        entry.setId(entity.getId());
        entry.setPatientId(entity.getPatientId());
        entry.setRdvId(entity.getRdvId());
        entry.setPatientNom(entity.getPatientNom());
        entry.setPatientPrenom(entity.getPatientPrenom());
        entry.setService(entity.getService());
        entry.setPosition(entity.getPosition());
        entry.setStatut(entity.getStatut());
        entry.setHeureArrivee(entity.getHeureArrivee());
        entry.setTempsEstime(entity.getTempsEstime());
        entry.setCreatedAt(entity.getCreatedAt());
        return entry;
    }
}
