package com.hacnation.patientidentity.domain.port;

import com.hacnation.patientidentity.domain.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepositoryPort {

    Patient save(Patient patient);

    Optional<Patient> findById(String id);

    Page<Patient> findAll(Pageable pageable);

    Page<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);

    Optional<Patient> findByTelephone(String telephone);

    List<Patient> findByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndDateNaissance(
            String nom, String prenom, LocalDate dateNaissance);

    void delete(Patient patient);
}
