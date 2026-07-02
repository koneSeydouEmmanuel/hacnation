package com.hacnation.dme.infrastructure.adapter.out.jpa;

import com.hacnation.dme.domain.model.Patient;
import com.hacnation.dme.domain.port.PatientReadPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PatientJpaRepository implements PatientReadPort {

    private final SpringPatientJpaRepository jpaRepository;

    public PatientJpaRepository(SpringPatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Patient> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Patient> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public Page<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable) {
        return jpaRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(nom, prenom, pageable)
                .map(this::toDomain);
    }

    private Patient toDomain(PatientEntity entity) {
        Patient patient = new Patient();
        patient.setId(entity.getId());
        patient.setNom(entity.getNom());
        patient.setPrenom(entity.getPrenom());
        patient.setDateNaissance(entity.getDateNaissance());
        patient.setSexe(entity.getSexe());
        patient.setTelephone(entity.getTelephone());
        patient.setEmail(entity.getEmail());
        patient.setAdresse(entity.getAdresse());
        patient.setGroupeSanguin(entity.getGroupeSanguin());
        patient.setStatut(entity.getStatut());
        patient.setCreatedAt(entity.getCreatedAt());
        patient.setUpdatedAt(entity.getUpdatedAt());
        return patient;
    }
}
