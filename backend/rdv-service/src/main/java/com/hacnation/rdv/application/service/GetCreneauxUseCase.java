package com.hacnation.rdv.application.service;

import com.hacnation.common.enums.StatutRdv;
import com.hacnation.rdv.domain.model.RendezVous;
import com.hacnation.rdv.domain.port.RdvRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GetCreneauxUseCase {

    private final RdvRepositoryPort rdvRepository;

    public GetCreneauxUseCase(RdvRepositoryPort rdvRepository) {
        this.rdvRepository = rdvRepository;
    }

    public List<LocalTime> execute(String service, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<RendezVous> booked = rdvRepository.findByServiceAndDateHeureBetween(service, startOfDay, endOfDay);

        Set<Integer> bookedHours = booked.stream()
                .filter(r -> r.getStatut() != StatutRdv.ANNULE)
                .map(r -> r.getDateHeure().getHour())
                .collect(Collectors.toSet());

        List<LocalTime> available = new ArrayList<>();
        for (int hour = 8; hour < 18; hour++) {
            if (!bookedHours.contains(hour)) {
                available.add(LocalTime.of(hour, 0));
            }
        }
        return available;
    }
}
