package com.hacnation.urgences.infrastructure.adapter.out.jpa;

import com.hacnation.urgences.domain.model.Triage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriageJpaRepository extends JpaRepository<Triage, String> {

    List<Triage> findByAdmissionId(String admissionId);
}
