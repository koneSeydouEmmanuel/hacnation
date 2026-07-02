package com.hacnation.dme.domain.port;

import com.hacnation.dme.domain.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PatientReadPort {

    Optional<Patient> findById(String id);

    Page<Patient> findAll(Pageable pageable);

    Page<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
}
