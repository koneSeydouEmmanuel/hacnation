package com.hacnation.patientidentity.infrastructure.adapter.out.jpa;

import com.hacnation.patientidentity.domain.model.Patient;
import com.hacnation.patientidentity.domain.port.PatientRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PatientJpaRepository implements PatientRepositoryPort {

    private final SpringPatientJpaRepository jpaRepository;

    public PatientJpaRepository(SpringPatientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = toEntity(patient);
        PatientEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
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

    @Override
    public Optional<Patient> findByTelephone(String telephone) {
        return jpaRepository.findByTelephone(telephone).map(this::toDomain);
    }

    @Override
    public List<Patient> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
            String nom, String prenom, LocalDate dateNaissance) {
        return jpaRepository.findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
                        nom, prenom, dateNaissance)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Patient patient) {
        jpaRepository.deleteById(patient.getId());
    }

    private PatientEntity toEntity(Patient patient) {
        PatientEntity entity = new PatientEntity();
        entity.setId(patient.getId());
        entity.setNom(patient.getNom());
        entity.setPrenom(patient.getPrenom());
        entity.setDateNaissance(patient.getDateNaissance());
        entity.setSexe(patient.getSexe());
        entity.setTelephone(patient.getTelephone());
        entity.setEmail(patient.getEmail());
        entity.setAdresse(patient.getAdresse());
        entity.setGroupeSanguin(patient.getGroupeSanguin());
        entity.setStatut(patient.getStatut());
        entity.setCreatedAt(patient.getCreatedAt());
        entity.setUpdatedAt(patient.getUpdatedAt());
        return entity;
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
