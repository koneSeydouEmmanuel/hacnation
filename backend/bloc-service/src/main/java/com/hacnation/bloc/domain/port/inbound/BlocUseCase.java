package com.hacnation.bloc.domain.port.inbound;

import com.hacnation.bloc.domain.model.InterventionChirurgicale;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BlocUseCase {

    InterventionChirurgicale programmerIntervention(String patientId, String typeIntervention,
                                                    String chirurgienId, String salle,
                                                    LocalDateTime dateHeure, Integer dureeEstimee);

    InterventionChirurgicale updateStatutIntervention(String id, String newStatut);

    List<InterventionChirurgicale> getPlanningSalle(String salle, LocalDate date);
}
