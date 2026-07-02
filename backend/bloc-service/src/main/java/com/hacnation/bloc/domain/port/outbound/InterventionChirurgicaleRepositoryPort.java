package com.hacnation.bloc.domain.port.outbound;

import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InterventionChirurgicaleRepositoryPort {

    InterventionChirurgicale save(InterventionChirurgicale intervention);

    Optional<InterventionChirurgicale> findById(String id);

    List<InterventionChirurgicale> findBySalleAndDateHeureBetween(String salle, LocalDateTime start, LocalDateTime end);
}
