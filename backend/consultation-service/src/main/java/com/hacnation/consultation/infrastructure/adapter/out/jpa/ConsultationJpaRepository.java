package com.hacnation.consultation.infrastructure.adapter.out.jpa;

import com.hacnation.consultation.domain.model.Consultation;
import com.hacnation.consultation.domain.port.ConsultationRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationJpaRepository extends JpaRepository<Consultation, String>, ConsultationRepositoryPort {

    Page<Consultation> findByPatientIdOrderByDateDesc(String patientId, Pageable pageable);

    Page<Consultation> findByMedecinIdOrderByDateDesc(String medecinId, Pageable pageable);
}
