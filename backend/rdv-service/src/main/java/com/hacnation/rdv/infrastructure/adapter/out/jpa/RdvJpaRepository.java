package com.hacnation.rdv.infrastructure.adapter.out.jpa;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RdvJpaRepository implements RdvRepositoryPort {

    private final SpringRdvJpaRepository jpaRepository;

    public RdvJpaRepository(SpringRdvJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RendezVous save(RendezVous rdv) {
        RendezVousEntity entity = toEntity(rdv);
        RendezVousEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RendezVous> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<RendezVous> findByPatientIdOrderByDateHeureDesc(String patientId) {
        return jpaRepository.findByPatientIdOrderByDateHeureDesc(patientId)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RendezVous> findByDateHeureBetween(LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByDateHeureBetween(start, end)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RendezVous> findByServiceAndDateHeureBetween(String service, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByServiceAndDateHeureBetween(service, start, end)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RendezVous> findByStatutAndDateHeureBetween(StatutRdv statut, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByStatutAndDateHeureBetween(statut, start, end)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<RendezVous> findByPraticienIdAndDateHeureBetween(String praticienId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByPraticienIdAndDateHeureBetween(praticienId, start, end)
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    private RendezVousEntity toEntity(RendezVous rdv) {
        RendezVousEntity entity = new RendezVousEntity();
        entity.setId(rdv.getId());
        entity.setPatientId(rdv.getPatientId());
        entity.setPraticienId(rdv.getPraticienId());
        entity.setService(rdv.getService());
        entity.setDateHeure(rdv.getDateHeure());
        entity.setStatut(rdv.getStatut());
        entity.setQrCode(rdv.getQrCode());
        entity.setMotif(rdv.getMotif());
        entity.setCreatedAt(rdv.getCreatedAt());
        entity.setUpdatedAt(rdv.getUpdatedAt());
        return entity;
    }

    private RendezVous toDomain(RendezVousEntity entity) {
        RendezVous rdv = new RendezVous();
        rdv.setId(entity.getId());
        rdv.setPatientId(entity.getPatientId());
        rdv.setPraticienId(entity.getPraticienId());
        rdv.setService(entity.getService());
        rdv.setDateHeure(entity.getDateHeure());
        rdv.setStatut(entity.getStatut());
        rdv.setQrCode(entity.getQrCode());
        rdv.setMotif(entity.getMotif());
        rdv.setCreatedAt(entity.getCreatedAt());
        rdv.setUpdatedAt(entity.getUpdatedAt());
        return rdv;
    }
}
