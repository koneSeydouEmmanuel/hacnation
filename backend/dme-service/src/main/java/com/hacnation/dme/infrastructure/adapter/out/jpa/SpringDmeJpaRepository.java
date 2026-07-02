package com.hacnation.dme.infrastructure.adapter.out.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface SpringDmeJpaRepository extends JpaRepository<DossierMedicalEntity, String> {

    Optional<DossierMedicalEntity> findByPatientId(String patientId);
}
