package com.hacnation.bloc.infrastructure.adapter.outbound;

import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import com.hacnation.bloc.domain.port.outbound.InterventionChirurgicaleRepositoryPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface InterventionChirurgicaleJpaRepository extends JpaRepository<InterventionChirurgicale, String> {

    List<InterventionChirurgicale> findBySalleAndDateHeureBetween(String salle, LocalDateTime start, LocalDateTime end);
}

@Repository
class InterventionChirurgicaleJpaAdapter implements InterventionChirurgicaleRepositoryPort {

    private final InterventionChirurgicaleJpaRepository jpaRepository;

    InterventionChirurgicaleJpaAdapter(InterventionChirurgicaleJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public InterventionChirurgicale save(InterventionChirurgicale intervention) {
        return jpaRepository.save(intervention);
    }

    @Override
    public Optional<InterventionChirurgicale> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<InterventionChirurgicale> findBySalleAndDateHeureBetween(String salle, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findBySalleAndDateHeureBetween(salle, start, end);
    }
}
