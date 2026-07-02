package com.hacnation.dme.infrastructure.adapter.out.jpa;

import com.hacnation.dme.domain.model.DossierMedical;
import com.hacnation.dme.domain.port.DmeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DmeJpaRepository implements DmeRepositoryPort {

    private final SpringDmeJpaRepository jpaRepository;

    public DmeJpaRepository(SpringDmeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public DossierMedical save(DossierMedical dme) {
        DossierMedicalEntity entity = toEntity(dme);
        DossierMedicalEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<DossierMedical> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<DossierMedical> findByPatientId(String patientId) {
        return jpaRepository.findByPatientId(patientId).map(this::toDomain);
    }

    private DossierMedicalEntity toEntity(DossierMedical dme) {
        DossierMedicalEntity entity = new DossierMedicalEntity();
        entity.setId(dme.getId());
        entity.setPatientId(dme.getPatientId());
        entity.setAntecedents(dme.getAntecedents());
        entity.setNotesCliniques(dme.getNotesCliniques());
        entity.setDocuments(dme.getDocuments());
        entity.setCreatedAt(dme.getCreatedAt());
        entity.setUpdatedAt(dme.getUpdatedAt());
        return entity;
    }

    private DossierMedical toDomain(DossierMedicalEntity entity) {
        DossierMedical dme = new DossierMedical();
        dme.setId(entity.getId());
        dme.setPatientId(entity.getPatientId());
        dme.setAntecedents(entity.getAntecedents());
        dme.setNotesCliniques(entity.getNotesCliniques());
        dme.setDocuments(entity.getDocuments());
        dme.setCreatedAt(entity.getCreatedAt());
        dme.setUpdatedAt(entity.getUpdatedAt());
        return dme;
    }
}
