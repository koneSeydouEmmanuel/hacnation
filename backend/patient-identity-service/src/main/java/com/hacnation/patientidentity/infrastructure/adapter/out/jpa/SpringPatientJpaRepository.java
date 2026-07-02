package com.hacnation.patientidentity.infrastructure.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
interface SpringPatientJpaRepository extends JpaRepository<PatientEntity, String> {

    org.springframework.data.domain.Page<PatientEntity> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(
            String nom, String prenom, org.springframework.data.domain.Pageable pageable);

    Optional<PatientEntity> findByTelephone(String telephone);

    List<PatientEntity> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
            String nom, String prenom, LocalDate dateNaissance);
}
