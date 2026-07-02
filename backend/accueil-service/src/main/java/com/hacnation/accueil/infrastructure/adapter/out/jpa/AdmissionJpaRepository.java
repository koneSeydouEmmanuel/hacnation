package com.hacnation.accueil.infrastructure.adapter.out.jpa;

import com.hacnation.accueil.domain.model.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionJpaRepository extends JpaRepository<Admission, String> {

    List<Admission> findByPatientId(String patientId);
}
